package com.cs.runtime.strategy.plugin.usecase.goldenrecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.config.strategy.plugin.usecase.goldenrecord.GoldenRecordRuleUtil;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.propertycollection.util.PropertyCollectionUtils;
import com.cs.config.strategy.plugin.usecase.relationship.util.GetRelationshipUtils;
import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variantcontext.util.VariantContextUtils;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollection;
import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;
import com.cs.core.config.interactor.entity.propertycollection.ISectionRelationship;
import com.cs.core.config.interactor.exception.taxonomy.KlassTaxonomyNotFoundException;
import com.cs.core.config.interactor.model.goldenrecord.IConfigDetailsForGetKlassInstancesToMergeModel;
import com.cs.core.config.interactor.model.goldenrecord.IGetConfigDetailsForComparisonRequestModel;
import com.cs.core.config.interactor.model.goldenrecord.IGetGoldenRecordRuleModel;
import com.cs.core.config.interactor.model.klass.IGetMultiClassificationKlassDetailsModel;
import com.cs.core.config.interactor.model.klass.IReferencedNatureRelationshipModel;
import com.cs.core.config.interactor.model.klass.IReferencedPropertyCollectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedPropertyCollectionModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionRelationshipModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextTagsModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipPropertiesModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.runtime.strategy.plugin.usecase.base.AbstractConfigDetails;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings("unchecked")
public class GetConfigDetailsForGetKlassInstancesToMerge extends AbstractConfigDetails{
  
  public GetConfigDetailsForGetKlassInstancesToMerge(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForGetKlassInstancesToMerge/*" };
  }

  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> mapToReturn = getMapToReturn();
    
    List<String> klassIds = (List<String>) requestMap.get(IGetConfigDetailsForComparisonRequestModel.KLASS_IDS);
    List<String> taxonomyIds = (List<String>) requestMap.get(IGetConfigDetailsForComparisonRequestModel.SELECTED_TAXONOMY_IDS);
    List<String> languageCodes = (List<String>) requestMap.get(IGetConfigDetailsForComparisonRequestModel.LANGUAGE_CODES);
    
    List<String> propertyIdsToExclude = new ArrayList<>();
    
    //This is for MxM promotional set matchAndMerge like use cases where you need to filter attributes and tags by some klass
    List<String> propertyIdsToFetch = getPropertyIdsToFetch(requestMap);
    
    fillKlassDetails(mapToReturn, klassIds,propertyIdsToExclude,propertyIdsToFetch);
    fillTaxonomyDetails(mapToReturn, taxonomyIds, propertyIdsToExclude, propertyIdsToFetch);
    List<String> allTypes = new ArrayList<>(klassIds);
    allTypes.addAll(taxonomyIds);
    mergeCouplingTypeFromOfReferencedElementsFromRelationship(mapToReturn, allTypes);
    String ruleId = (String) requestMap.get(IGetConfigDetailsForComparisonRequestModel.RULE_ID);
    fillGoldenRecordRule(mapToReturn, ruleId);
    fillDependentAttributeIds(mapToReturn);
    fillReferencedLanguages(mapToReturn, languageCodes);
    return mapToReturn;
  }
  
  private void fillDependentAttributeIds(Map<String, Object> mapToReturn)
  {
    Map<String, Object> referencedAttributeMap = (Map<String, Object>) mapToReturn
        .get(IConfigDetailsForGetKlassInstancesToMergeModel.REFERENCED_ATTRIBUTES);
    List<String> attributeIds = referencedAttributeMap.entrySet()
        .stream()
        .filter(entry -> ((Map<String, Object>) entry.getValue()).get(IAttribute.IS_TRANSLATABLE).equals(true))
        .map(entry -> entry.getKey()).collect(Collectors.toList());
    mapToReturn.put(IConfigDetailsForGetKlassInstancesToMergeModel.DEPENDEND_ATTRIBUTE_IDS, attributeIds);
  }

  protected List<String> getPropertyIdsToFetch(Map<String, Object> requestMap) throws Exception
  {
    return null;
  }

  private Map<String, Object> getMapToReturn()
  {
    Map<String, Object> mapToReturn = new HashMap<>();
    Map<String, Object> referencedKlassMap = new HashMap<>();
    Map<String, Object> referencedAttributeMap = new HashMap<>();
    Map<String, Object> referencedTagMap = new HashMap<>();
    Map<String, Object> referencedTaxonomies = new HashMap<>();
    Map<String, Object> referencedNatureRelationships = new HashMap<>();
    Map<String, Object> referencedRelationshipMap = new HashMap<>();
    Map<String,Object> referencedPropertyCollections = new HashMap<>();
    mapToReturn.put(IConfigDetailsForGetKlassInstancesToMergeModel.REFRENCED_KLASSES, referencedKlassMap);
    mapToReturn.put(IConfigDetailsForGetKlassInstancesToMergeModel.REFERENCED_ATTRIBUTES, referencedAttributeMap);
    mapToReturn.put(IConfigDetailsForGetKlassInstancesToMergeModel.REFERENCED_TAGS, referencedTagMap);
    mapToReturn.put(IConfigDetailsForGetKlassInstancesToMergeModel.REFERENCED_TAXONOMIES, referencedTaxonomies);
    mapToReturn.put(IConfigDetailsForGetKlassInstancesToMergeModel.REFERENCED_RELATIONSHIPS, referencedRelationshipMap);
    mapToReturn.put(IConfigDetailsForGetKlassInstancesToMergeModel.REFERENCED_NATURE_RELATIONSHIPS, referencedNatureRelationships);
    mapToReturn.put(IConfigDetailsForGetKlassInstancesToMergeModel.REFERENCED_ELEMENTS, new HashMap<>());
    mapToReturn.put(IConfigDetailsForGetKlassInstancesToMergeModel.GOLDEN_RECORD_RULE, new HashMap<>());
    mapToReturn.put(IConfigDetailsForGetKlassInstancesToMergeModel.DEPENDEND_ATTRIBUTE_IDS, new ArrayList<>());
    mapToReturn.put(IConfigDetailsForGetKlassInstancesToMergeModel.REFERENCED_PROPERTY_COLLECTIONS, referencedPropertyCollections);
    mapToReturn.put(IConfigDetailsForGetKlassInstancesToMergeModel.REFERENCED_RELATIONSHIPS_PROPERTIES, new HashMap<>());
    mapToReturn.put(IConfigDetailsForGetKlassInstancesToMergeModel.REFERENCED_LANGUAGES, new HashMap<>());
    
    Map<String, Object> referencedVariantContextsMap = new HashMap<>();
    Map<String, Object> embeddedVariantContexts = new HashMap<>();
    Map<String, Object> languageVariantContexts = new HashMap<>();
    Map<String, Object> productVariantContexts = new HashMap<>();
    Map<String, Object> versionContexts = new HashMap<>();
    referencedVariantContextsMap.put(IReferencedContextModel.EMBEDDED_VARIANT_CONTEXTS, embeddedVariantContexts);
    referencedVariantContextsMap.put(IReferencedContextModel.LANGUAGE_VARIANT_CONTEXTS, languageVariantContexts);
    referencedVariantContextsMap.put(IReferencedContextModel.PRODUCT_VARIANT_CONTEXTS, productVariantContexts);
   // referencedVariantContextsMap.put(IReferencedContextModel.VERSION_CONTEXTS, versionContexts);
    
    mapToReturn.put(IConfigDetailsForGetKlassInstancesToMergeModel.REFERENCED_VARIANT_CONTEXTS, referencedVariantContextsMap);
    
    return mapToReturn;
  }
  
  private void fillKlassDetails(Map<String, Object> mapToReturn,List<String> klassIds,List<String> propertyIdsToExclude, List<String> propertyIdsToFetch) throws Exception
  {
    Map<String,Object> referencedKlassMap = (Map<String, Object>) mapToReturn.get(IConfigDetailsForGetKlassInstancesToMergeModel.REFRENCED_KLASSES);
    for (String klassId : klassIds) {
      try {
        Vertex klassVertex = UtilClass.getVertexByIndexedId(klassId, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
        List<String> fieldsToFetch = Arrays.asList(
            IKlass.NUMBER_OF_VERSIONS_TO_MAINTAIN, IKlass.ICON, IKlass.LABEL, IKlass.TYPE,
            IKlass.NATURE_TYPE, IKlass.IS_NATURE, IKlass.CODE);
        Map<String, Object> klassMap = UtilClass.getMapFromVertex(fieldsToFetch, klassVertex);
        fillReferencedElements(mapToReturn, klassVertex, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS,  propertyIdsToExclude, propertyIdsToFetch);
        fillReferencedPropertyCollections(klassVertex, mapToReturn, null);
        Boolean isNature = klassVertex.getProperty(IKlass.IS_NATURE);
        if (isNature != null && isNature) {
          fillReferencedNatureRelationships(mapToReturn, klassVertex, propertyIdsToFetch);
        }
        referencedKlassMap.put(klassId, klassMap);
      } catch (NotFoundException e) {
        throw new KlassNotFoundException(e);
      }
    }
  }
  
  private void fillReferencedElements(Map<String, Object> mapToReturn, Vertex klassVertex,
      String vertexLabel , List<String> propertyIdsToExclude, List<String> propertyIdsToFetch) throws Exception
  {
    Map<String, Object> referencedElements = (Map<String, Object>) mapToReturn.get(IConfigDetailsForGetKlassInstancesToMergeModel.REFERENCED_ELEMENTS);
    Map<String, Object> referencedAttributes = (Map<String, Object>) mapToReturn.get(IConfigDetailsForGetKlassInstancesToMergeModel.REFERENCED_ATTRIBUTES);
    Map<String, Object> referencedTags = (Map<String, Object>) mapToReturn.get(IConfigDetailsForGetKlassInstancesToMergeModel.REFERENCED_TAGS);
    Map<String, Object> referencedRelationships = (Map<String, Object>) mapToReturn.get(IConfigDetailsForGetKlassInstancesToMergeModel.REFERENCED_RELATIONSHIPS);
    Map<String, Object> referencedRelationshipProperties = (Map<String, Object>) mapToReturn.get(IConfigDetailsForGetKlassInstancesToMergeModel.REFERENCED_RELATIONSHIPS_PROPERTIES);
    String klassId = UtilClass.getCId(klassVertex);
    
    Iterable<Vertex> kPNodesIterable = klassVertex.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_KLASS_PROPERTY);
    for (Vertex klassPropertyNode : kPNodesIterable) {
     
      String entityId = klassPropertyNode.getProperty(ISectionElement.PROPERTY_ID);
      if(propertyIdsToFetch!=null && !propertyIdsToFetch.contains(entityId))
      {
        continue;
      }
      
      if(propertyIdsToExclude.contains(entityId)) {
        continue;
      }
      Iterator<Vertex> attributeContextsIterator = klassPropertyNode.getVertices(Direction.IN, RelationshipLabelConstants.VARIANT_CONTEXT_OF).iterator();
      if (attributeContextsIterator.hasNext()) {
        propertyIdsToExclude.add(entityId);
        continue;
      }
      
      String entityType = klassPropertyNode.getProperty(CommonConstants.TYPE);
      Vertex propertyNode = null;
      Map<String, Object> referencedElementMap = UtilClass.getMapFromNode(klassPropertyNode);
      referencedElementMap.put(CommonConstants.ID_PROPERTY, entityId);
      
      if (entityType.equals(SystemLevelIds.PROPERTY_TYPE_TAG)) {
        if(!referencedTags.containsKey(entityId)) {
          propertyNode = UtilClass.getVertexByIndexedId(entityId, VertexLabelConstants.ENTITY_TAG);
          Map<String, Object> tagMap = TagUtils.getTagMap(propertyNode, false);
          referencedTags.put(entityId, tagMap);
        }
      }
      
      if (entityType.equals(SystemLevelIds.PROPERTY_TYPE_ATTRIBUTE)) {
        if(!referencedAttributes.containsKey(entityId)) {
          propertyNode = UtilClass.getVertexByIndexedId(entityId, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
          Map<String, Object> attributeMap = AttributeUtils.getAttributeMap(propertyNode);
          referencedAttributes.put(entityId, attributeMap);
        }
      }
      if (entityType.equals(SystemLevelIds.PROPERTY_TYPE_RELATIONSHIP)) {
        Boolean isNature = klassPropertyNode.getProperty(ISectionRelationship.IS_NATURE);
        /*if(isNature) {
          //continue;
        }
        */
        referencedElementMap.put(CommonConstants.ID_PROPERTY, UtilClass.getCId(klassPropertyNode));
        propertyNode = UtilClass.getVertexByIndexedId(entityId, VertexLabelConstants.ROOT_RELATIONSHIP);
        Map<String, Object> side = (Map<String, Object>) referencedElementMap.get(IReferencedSectionRelationshipModel.RELATIONSHIP_SIDE);
        if (side != null) {
          side.put(CommonConstants.RELATIONSHIP_MAPPING_ID_PROPERTY, UtilClass.getCId(klassPropertyNode));
        }
        Map<String, Object> relationshipMap = RelationshipUtils.getRelationshipMapWithContext(propertyNode);
        relationshipMap.remove(IReferencedNatureRelationshipModel.RELATIONSHIP_TYPE);
        relationshipMap.remove(IReferencedNatureRelationshipModel.AUTO_CREATE_SETTINGS);
        relationshipMap.remove(IReferencedNatureRelationshipModel.MAX_NO_OF_ITEMS);
        relationshipMap.remove(IReferencedNatureRelationshipModel.TAXONOMY_INHERITANCE_SETTING);
        referencedRelationships.put(entityId, relationshipMap);
        referencedElements.put(UtilClass.getCId(klassPropertyNode), referencedElementMap);
        
        Map<String,Object> relationshipPropertiesMap = new HashMap<>();
        String label = (String) UtilClass.getValueByLanguage(propertyNode,CommonConstants.LABEL_PROPERTY);
        relationshipPropertiesMap.put(IReferencedRelationshipPropertiesModel.LABEL, label);
        RelationshipUtils.populatePropetiesInfoForGoldenRecord(propertyNode, relationshipPropertiesMap);
        referencedRelationshipProperties.put((String) klassPropertyNode.getProperty(ISectionRelationship.PROPERTY_ID), relationshipPropertiesMap);
        continue;
      }
      
      if (referencedElements.containsKey(entityId)) {
        Map<String, Object> existingReferencedElement = (Map<String, Object>) referencedElements.get(entityId);
        mergeReferencedElement(referencedElementMap, existingReferencedElement, klassId, entityType,false);
      }
      else {
        referencedElements.put(entityId, referencedElementMap);
      }
    }
  }
  
  private void fillReferencedNatureRelationships(Map<String, Object> mapToReturn, Vertex klassNode, List<String> propertyIdsToFetch) throws Exception
  {
    Map<String, Object> referencedNatureRelationshipMap = (Map<String, Object>) mapToReturn.get(IConfigDetailsForGetKlassInstancesToMergeModel.REFERENCED_NATURE_RELATIONSHIPS);
    
    Map<String, Object> referencedElementMap = (Map<String, Object>) mapToReturn.get(IConfigDetailsForGetKlassInstancesToMergeModel.REFERENCED_ELEMENTS);
    Iterable<Edge> klassNatureRelationshipOfEdges = klassNode.getEdges(Direction.OUT, RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF);
    for (Edge klassNatureRelationshipOfEdge : klassNatureRelationshipOfEdges) {
      Vertex klassNatureRelationshipVertex = klassNatureRelationshipOfEdge.getVertex(Direction.IN);
      Iterable<Vertex> natureRelationshipNodes = klassNatureRelationshipVertex.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY);
      for (Vertex natureRelationshipNode : natureRelationshipNodes) {
        String natureRelationshipId = UtilClass.getCId(natureRelationshipNode);
        if(propertyIdsToFetch!=null && !propertyIdsToFetch.contains(natureRelationshipId))
        {
          continue;
        }
        //Adding Referenced Element
        Map<String, Object> referencedElement = UtilClass.getMapFromNode(klassNatureRelationshipVertex);
        String natureType = (String) referencedElement.remove(IReferencedNatureRelationshipModel.NATURE_TYPE);
        String relationshipType = (String) referencedElement.remove(IReferencedNatureRelationshipModel.RELATIONSHIP_TYPE);
        referencedElement.put(IReferencedSectionRelationshipModel.ID, UtilClass.getCId(klassNatureRelationshipVertex));
        referencedElement.put(IReferencedSectionRelationshipModel.IS_DISABLED, false);
        Map<String, Object> side = (Map<String, Object>) referencedElement.get(IReferencedSectionRelationshipModel.RELATIONSHIP_SIDE);
        
        GetRelationshipUtils.prepareSideMapTranslation(side);
        
        //Adding Referenced Nature Relationship
        Map<String, Object> relationshipMap = RelationshipUtils.getNatureRelationshipMap(natureRelationshipNode);
        relationshipMap.put(IReferencedNatureRelationshipModel.NATURE_TYPE, natureType);
        if (side != null) {
          side.put(CommonConstants.RELATIONSHIP_MAPPING_ID_PROPERTY, UtilClass.getCId(klassNatureRelationshipVertex));
        }
        referencedNatureRelationshipMap.put(natureRelationshipId, relationshipMap);
        referencedElementMap.put(UtilClass.getCId(klassNatureRelationshipVertex), referencedElement);
        Map<String, Object> referencedVariantContextMap = (Map<String, Object>) mapToReturn.get(IGetConfigDetailsForCustomTabModel.REFERENCED_VARIANT_CONTEXTS);
        if(relationshipType != null && relationshipType.equals(CommonConstants.PRODUCT_VARIANT_RELATIONSHIP)) {
          fillVariantContextsOfKlass(klassNatureRelationshipVertex, referencedVariantContextMap, mapToReturn, IReferencedContextModel.PRODUCT_VARIANT_CONTEXTS);
        }
        if(relationshipType != null && relationshipType.equals(CommonConstants.VERSION_CONTEXT_RELATIONSHIP)) {
          fillVersionContexts(mapToReturn, klassNatureRelationshipVertex);
        }
      } 
    }
  }
  
  private void fillTaxonomyDetails(Map<String, Object> mapToReturn, List<String> taxonomyIds, List<String> propertyIdsToExclude, List<String> propertyIdsToFetch)
      throws Exception
  {
    Map<String,Object> referencedTaxonomyMap = (Map<String, Object>) mapToReturn.get(IConfigDetailsForGetKlassInstancesToMergeModel.REFERENCED_TAXONOMIES);
    for (String taxonomyId : taxonomyIds) {
      try {
        Vertex taxonomyVertex = UtilClass.getVertexByIndexedId(taxonomyId, VertexLabelConstants.ROOT_KLASS_TAXONOMY);
        Map<String, Object> taxonomyMap = UtilClass.getMapFromVertex(
            Arrays.asList(IReferencedArticleTaxonomyModel.LABEL,
                IReferencedArticleTaxonomyModel.ICON, IReferencedArticleTaxonomyModel.TAXONOMY_TYPE,
                IReferencedArticleTaxonomyModel.CODE, IReferencedArticleTaxonomyModel.BASETYPE), taxonomyVertex);
        
        fillTaxonomiesChildrenAndParentData(taxonomyMap,taxonomyVertex);
        fillReferencedElements(mapToReturn, taxonomyVertex, VertexLabelConstants.ROOT_KLASS_TAXONOMY,propertyIdsToExclude, propertyIdsToFetch);
        fillReferencedPropertyCollections(taxonomyVertex, mapToReturn, null);
        referencedTaxonomyMap.put(taxonomyId, taxonomyMap);
      } catch (NotFoundException e) {
        throw new KlassTaxonomyNotFoundException(e);
      }
    }
  }
  
  private void fillGoldenRecordRule(Map<String, Object> mapToReturn, String ruleId) throws Exception
  {
    if (ruleId == null) {
      return;
    }
    Vertex goldenRecordRule = UtilClass.getVertexByIndexedId(ruleId, VertexLabelConstants.GOLDEN_RECORD_RULE);
    Map<String,Object> goldenRecordRuleMap = GoldenRecordRuleUtil.getGoldenRecordRuleFromNode(goldenRecordRule);
    goldenRecordRuleMap.remove(IGetGoldenRecordRuleModel.REFERENCED_ATTRIBUTES);
    goldenRecordRuleMap.remove(IGetGoldenRecordRuleModel.REFERENCED_TAGS);
    goldenRecordRuleMap.remove(IGetGoldenRecordRuleModel.REFERENCED_KLASSES);
    goldenRecordRuleMap.remove(IGetGoldenRecordRuleModel.REFERENCED_TAXONOMIES);
    goldenRecordRuleMap.remove(IGetGoldenRecordRuleModel.REFERENCED_RELATIONSHIPS);
    goldenRecordRuleMap.remove(IGetGoldenRecordRuleModel.REFERENCED_NATURE_RELATIONSHIPS);
    goldenRecordRuleMap.remove(IGetGoldenRecordRuleModel.REFERENCED_ORGANIZATIONS);
    goldenRecordRuleMap.remove(IGetGoldenRecordRuleModel.REFERENCED_ENDPOINTS);
    mapToReturn.put(IConfigDetailsForGetKlassInstancesToMergeModel.GOLDEN_RECORD_RULE,
        goldenRecordRuleMap.get(IGetGoldenRecordRuleModel.GOLDEN_RECORD_RULE));
  }
  
  protected void fillVariantContextsOfKlass(Vertex klassNode,
      Map<String, Object> referencedVariantContextsMap, Map<String, Object> mapToReturn, String linkedVariantKey) throws Exception
  {
    Map<String, Object> linkedVariantContexts = (Map<String, Object>) referencedVariantContextsMap.get(linkedVariantKey);
    
    Map<String, Object> referencedTagsMap = (Map<String, Object>) mapToReturn.get(IGetMultiClassificationKlassDetailsModel.REFERENCED_TAGS);
    
    Iterable<Edge> variantContextEdges = klassNode.getEdges(Direction.OUT, RelationshipLabelConstants.VARIANT_CONTEXT_OF);
    for (Edge variantContextEdge : variantContextEdges) {
      Vertex variantContextNode = variantContextEdge.getVertex(Direction.IN);
      
      Map<String, Object> variantContextMap = VariantContextUtils.getReferencedContexts(variantContextNode);
      
      String variantContextId = (String) variantContextMap.get(IReferencedVariantContextModel.ID);
      for (Map<String, Object> variantContext : (List<Map<String, Object>>) variantContextMap.get(IReferencedVariantContextModel.TAGS)) {
        String entityId = (String) variantContext.get(IReferencedVariantContextTagsModel.TAG_ID);
        Map<String, Object> entity = (Map<String, Object>) referencedTagsMap.get(entityId);
        if (entity == null) {
          try {
            Vertex entityNode = UtilClass.getVertexById(entityId, VertexLabelConstants.ENTITY_TAG);
            entity = TagUtils.getTagMap(entityNode, false);
            referencedTagsMap.put(entityId, entity);
          }
          catch (NotFoundException e) {
            // Do nothing;
          }
        }
      }
      if (!linkedVariantContexts.containsKey(variantContextId)) {
        linkedVariantContexts.put(variantContextId, variantContextMap);
      }
    }
  }
  
  protected void fillVersionContexts(Map<String, Object> mapToReturn, Vertex kNRNode) throws Exception
  {
    Map<String, Object> referencedVariantContext = (Map<String, Object>) mapToReturn.get(IGetConfigDetailsForCustomTabModel.REFERENCED_VARIANT_CONTEXTS);
    
    Map<String, Object> referencedTagsMap = (Map<String, Object>) mapToReturn.get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAGS);
    
    if(referencedVariantContext==null)
    {
      referencedVariantContext = new HashMap<String, Object>();
    }
    Map<String, Object> referencedVersionContexts = (Map<String, Object>) referencedVariantContext.get(IReferencedContextModel.VERSION_CONTEXTS);
    
    Iterable<Vertex> vertices = kNRNode.getVertices(Direction.OUT, RelationshipLabelConstants.VARIANT_CONTEXT_OF);
    
    for (Vertex contextNode : vertices) {
      String contextId = UtilClass.getCId(contextNode);
      if(referencedVersionContexts!=null)
      {
        Map<String, Object> variantContextMap = VariantContextUtils.getReferencedContexts(contextNode);
        
        for (Map<String, Object> contextTags : (List<Map<String, Object>>) variantContextMap.get(IReferencedVariantContextModel.TAGS)) {
          String entityId = (String) contextTags.get(IReferencedVariantContextTagsModel.TAG_ID);
          Map<String, Object> entity = (Map<String, Object>) referencedTagsMap.get(entityId);
          if (entity == null) {
            Vertex entityNode = UtilClass.getVertexById(entityId, VertexLabelConstants.ENTITY_TAG);
            entity = TagUtils.getTagMap(entityNode, false);
            referencedTagsMap.put(entityId, entity);
          }
        }
        referencedVersionContexts.put(contextId, variantContextMap);
      }
    }
  }
  
  protected void fillReferencedPropertyCollections(Vertex klassVertex, Map<String, Object> mapToReturn, List<String> propertiesToFetch) throws Exception
  {
    
    Map<String, Object> referencedPropertyCollections = (Map<String, Object>) mapToReturn.get(IGetConfigDetailsForCustomTabModel.REFERENCED_PROPERTY_COLLECTIONS);
    Iterable<Vertex> sectionVertices = klassVertex.getVertices(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF);
    for (Vertex sectionVertex : sectionVertices) {
      Vertex propertyCollectionVertex = KlassUtils.getPropertyCollectionNodeFromKlassSectionNode(sectionVertex);
      String propertyCollectionId = UtilClass.getCId(propertyCollectionVertex);
      if (referencedPropertyCollections.containsKey(propertyCollectionId)) {
        continue;
      }
        
      Map<String, Object> referencedPropertyCollection = UtilClass.getMapFromVertex(new ArrayList<>(), propertyCollectionVertex);
      if (propertiesToFetch != null && !propertiesToFetch.isEmpty()) {
        List<String> tempAttributeIds = new ArrayList<>(propertiesToFetch);
        List<String> tempTagIds = new ArrayList<>(propertiesToFetch);
        tempAttributeIds.retainAll((List<String>) referencedPropertyCollection.get(IPropertyCollection.ATTRIBUTE_IDS));
        tempTagIds.retainAll((List<String>) referencedPropertyCollection.get(IPropertyCollection.TAG_IDS));
        if (tempAttributeIds.isEmpty() && tempTagIds.isEmpty()) {
          continue;
        }
      }
        
      referencedPropertyCollections.put(propertyCollectionId, referencedPropertyCollection);
      List<Map<String, Object>> elementsList = new ArrayList<Map<String, Object>>();
      Iterable<Edge> entityToRelationships = propertyCollectionVertex.getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_ENTITY_TO);
      Set<String> entityIds = new HashSet<>();
      for (Edge entityTo : entityToRelationships) {
          Vertex entityVertex = entityTo.getVertex(Direction.OUT);
          String entityId = UtilClass.getCodeNew(entityVertex);
          Map<String, Object> propertyCollectionElement = new HashMap<String, Object>();
          propertyCollectionElement.put(IReferencedPropertyCollectionElementModel.ID, entityId);
          elementsList.add(propertyCollectionElement);
          entityIds.add(entityId);
        }
        List<String> propertySequenceIds = propertyCollectionVertex.getProperty(CommonConstants.PROPERTY_SEQUENCE_IDS);
        PropertyCollectionUtils.sortPropertyCollectionList(elementsList, propertySequenceIds,CommonConstants.ID_PROPERTY);
        referencedPropertyCollection.put(IReferencedPropertyCollectionModel.ELEMENTS, elementsList);    }
  }
}
