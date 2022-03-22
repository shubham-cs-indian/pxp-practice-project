package com.cs.runtime.strategy.plugin.usecase.base.klassinstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.IteratorUtils;

import com.cs.config.standard.IStandardConfig;
import com.cs.config.strategy.plugin.model.GetConfigDetailsHelperModelForComparingInstances;
import com.cs.config.strategy.plugin.model.IGetConfigDetailsHelperModelForComparingInstances;
import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.ConfigDetailsUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.MultiClassificationUtils;
import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variantcontext.util.VariantContextUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.exception.attribute.AttributeNotFoundException;
import com.cs.core.config.interactor.exception.taxonomy.KlassTaxonomyNotFoundException;
import com.cs.core.config.interactor.model.klass.IGetMultiClassificationKlassDetailsModel;
import com.cs.core.config.interactor.model.klass.IReferencedNatureRelationshipModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextTagsModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.runtime.strategy.plugin.usecase.base.AbstractConfigDetails;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings("unchecked")
public class GetConfigDetailsForGetKlassInstancesToCompare extends AbstractConfigDetails {
  
  public GetConfigDetailsForGetKlassInstancesToCompare(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForGetKlassInstancesToCompare/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    IGetConfigDetailsHelperModelForComparingInstances helperModel = new GetConfigDetailsHelperModelForComparingInstances();
    Map<String, Object> mapToReturn = getMapToReturn();
    
    List<String> klassIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.KLASS_IDS);
    List<String> taxonomyIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.SELECTED_TAXONOMY_IDS);
    List<String> collectionIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.COLLECTION_IDS);
    if (!klassIds.isEmpty()) {
      fillReferencedKlasses(mapToReturn, klassIds, helperModel);
      fillReferencedAttributes(helperModel.getNatureNodes(), mapToReturn);
      fillReferencedAttributes(helperModel.getNonNatureNodes(), mapToReturn);
      fillReferencedTags(helperModel.getNatureNodes(), mapToReturn);
      fillReferencedTags(helperModel.getNonNatureNodes(), mapToReturn);
      fillReferencedRoles(helperModel.getNatureNodes(), mapToReturn);
      fillReferencedRoles(helperModel.getNonNatureNodes(), mapToReturn);
      fillReferencedNatureRelationships(helperModel.getNatureNodes(), mapToReturn);
      fillRelationships(helperModel.getNatureNodes(), mapToReturn);
      fillRelationships(helperModel.getNonNatureNodes(), mapToReturn);
      fillReferenceRelationshipVariantContext(mapToReturn);
    }
    if (!taxonomyIds.isEmpty()) {
      fillTaxonomyDetails(mapToReturn, taxonomyIds, helperModel);
      fillReferencedAttributes(helperModel.getTaxonomyVertices(), mapToReturn);
      fillReferencedTags(helperModel.getTaxonomyVertices(), mapToReturn);
      fillReferencedRoles(helperModel.getTaxonomyVertices(), mapToReturn);
    }
    if (!collectionIds.isEmpty()) {
      fillHelperModelWithCollectionNodes(collectionIds, helperModel);
      fillReferencedAttributes(helperModel.getCollectionNodes(), mapToReturn);
      fillReferencedTags(helperModel.getCollectionNodes(), mapToReturn);
      fillReferencedRoles(helperModel.getCollectionNodes(), mapToReturn);
    }
    fillMandatoryReferencedAttributes(mapToReturn);
    
    return mapToReturn;
  }
  
  private void fillHelperModelWithCollectionNodes(List<String> collectionIds,
      IGetConfigDetailsHelperModelForComparingInstances helperModel)
  {
    OrientGraph graph = UtilClass.getGraph();
    String query = "select from " + VertexLabelConstants.COLLECTION + " where code in "
        + EntityUtil.quoteIt(collectionIds);
    Iterable<Vertex> collectionVertices = graph.command(new OCommandSQL(query))
        .execute();
    Iterator<Vertex> collectionVerticesIterator = collectionVertices.iterator();
    List<Vertex> collectionVertexList = IteratorUtils.toList(collectionVerticesIterator);
    helperModel.setCollectionNodes(new HashSet<>(collectionVertexList));
  }
  
  private void fillReferencedAttributes(Set<Vertex> klassNodes, Map<String, Object> mapToReturn) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Map<String, Object> referencedAttributes = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_ATTRIBUTES);
    Map<String, Object> referencedTags = (Map<String, Object>) mapToReturn.get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAGS);
    String query = "select from " + VertexLabelConstants.KLASS_ATTRIBUTE + " where in('"
        + RelationshipLabelConstants.HAS_KLASS_PROPERTY + "').code in "
        + EntityUtil.quoteIt(UtilClass.getCodes(new ArrayList<>(klassNodes)));
    
    Iterable<Vertex> klassAttributes = graph.command(new OCommandSQL(query))
        .execute();
    for (Vertex klassAttribute : klassAttributes) {
      Iterator<Vertex> attributeVertexIterator = klassAttribute
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
          .iterator();
      Vertex attributeVertex = attributeVertexIterator.next();
      if (!referencedAttributes.containsKey(UtilClass.getCodeNew(attributeVertex))) {
        Map<String, Object> attributeMap = AttributeUtils.getAttributeMap(attributeVertex);
        AttributeUtils.fillDependentAttributesForCalculatedOrConcatenatedAttributes(referencedAttributes, referencedTags, attributeMap);
        referencedAttributes.put(UtilClass.getCodeNew(attributeVertex), attributeMap);
      }
    }
  }
  
  private void fillReferencedTags(Set<Vertex> klassNodes, Map<String, Object> mapToReturn)
      throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Map<String, Object> referencedTags = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAGS);
    String query = "select from " + VertexLabelConstants.KLASS_TAG + " where in('"
        + RelationshipLabelConstants.HAS_KLASS_PROPERTY + "').code in "
        + EntityUtil.quoteIt(UtilClass.getCodes(new ArrayList<>(klassNodes)));
    Iterable<Vertex> klassTags = graph.command(new OCommandSQL(query))
        .execute();
    for (Vertex klassTag : klassTags) {
      Iterator<Vertex> tagVertexIterator = klassTag
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
          .iterator();
      Vertex tagVertex = tagVertexIterator.next();
      if (!referencedTags.containsKey(UtilClass.getCodeNew(tagVertex))) {
        Map<String, Object> tagMap = TagUtils.getTagMap(tagVertex, false);
        referencedTags.put(UtilClass.getCodeNew(tagVertex), tagMap);
      }
    }
  }
  
  private void fillReferencedRoles(Set<Vertex> klassNodes, Map<String, Object> mapToReturn)
  {
    OrientGraph graph = UtilClass.getGraph();
    Map<String, Object> referencedRoles = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_ROLES);
    String query = "select from " + VertexLabelConstants.KLASS_ROLE + " where in('"
        + RelationshipLabelConstants.HAS_KLASS_PROPERTY + "').code in "
        + EntityUtil.quoteIt(UtilClass.getCodes(new ArrayList<>(klassNodes)));
    Iterable<Vertex> klassRoles = graph.command(new OCommandSQL(query))
        .execute();
    for (Vertex klassRole : klassRoles) {
      Iterator<Vertex> roleVertexIterator = klassRole
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
          .iterator();
      Vertex roleVertex = roleVertexIterator.next();
      if (!referencedRoles.containsKey(UtilClass.getCodeNew(roleVertex))) {
        Map<String, Object> roleMap = RoleUtils.getRoleEntityMap(roleVertex);
        ;
        referencedRoles.put(UtilClass.getCodeNew(roleVertex), roleMap);
      }
    }
  }
  
  private void fillReferencedKlasses(Map<String, Object> mapToReturn, List<String> klassIds,
      IGetConfigDetailsHelperModelForComparingInstances helperModel) throws Exception
  {
    Map<String, Object> referencedKlassMap = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_KLASSES);
    for (String klassId : klassIds) {
      Vertex klassVertex = null;
      try {
        klassVertex = UtilClass.getVertexById(klassId, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
      Boolean isNature = klassVertex.getProperty(IKlass.IS_NATURE);
      if (isNature) {
        helperModel.getNatureNodes()
            .add(klassVertex);
      }
      else {
        helperModel.getNonNatureNodes()
            .add(klassVertex);
      }
      List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY, IKlass.CODE,
          IKlass.NUMBER_OF_VERSIONS_TO_MAINTAIN, IKlass.ICON, IKlass.LABEL, IKlass.TYPE,
          IKlass.NATURE_TYPE, IKlass.IS_NATURE);
      Map<String, Object> klassMap = UtilClass.getMapFromVertex(fieldsToFetch, klassVertex);
      
      Integer numberOfVersionsToMaintain = (Integer) klassMap
          .get(IKlass.NUMBER_OF_VERSIONS_TO_MAINTAIN);
      Integer existingNumberOfVersionsToMaintain = (Integer) mapToReturn
          .get(IGetConfigDetailsForCustomTabModel.NUMBER_OF_VERSIONS_TO_MAINTAIN);
      if (numberOfVersionsToMaintain > existingNumberOfVersionsToMaintain) {
        mapToReturn.put(IGetConfigDetailsForCustomTabModel.NUMBER_OF_VERSIONS_TO_MAINTAIN,
            numberOfVersionsToMaintain);
      }
      
      ConfigDetailsUtils.fillReferencedTagsAndLifeCycleStatusTags(mapToReturn, klassVertex);
      referencedKlassMap.put(klassId, klassMap);
    }
  }
  
  protected void fillMandatoryReferencedAttributes(Map<String, Object> mapToReturn) throws Exception
  {
    Map<String, Object> referencedAttributes = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsModel.REFERENCED_ATTRIBUTES);
    for (String attributeId : IStandardConfig.StandardProperty.MandatoryAttributeCodes) {
      Vertex attributeNode = null;
      try {
        attributeNode = UtilClass.getVertexById(attributeId,
            VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
      }
      catch (NotFoundException e) {
        throw new AttributeNotFoundException();
      }
      Map<String, Object> attribute = AttributeUtils.getAttributeMap(attributeNode);
      referencedAttributes.put(attributeId, attribute);
    }
  }
  
  private void fillReferencedNatureRelationships(Set<Vertex> natureKlassNodes,
      Map<String, Object> mapToReturn) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Map<String, Object> referencedNatureRelationshipMap = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_NATURE_RELATIONSHIPS);
    Map<String, String> referencedRelationshipsMapping = (Map<String, String>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_RELATIONSHIPS_MAPPING);
    Map<String, Object> referencedTagsMap = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAGS);
    Map<String, Object> referencedElements = (Map<String, Object>) mapToReturn
        .get(IGetMultiClassificationKlassDetailsModel.REFERENCED_ELEMENTS);
    
    for (Vertex natureKlassNode : natureKlassNodes) {
      String natureKlassId = UtilClass.getCodeNew(natureKlassNode);
      String query = "select from (select expand(in("
          + EntityUtil.quoteIt(RelationshipLabelConstants.HAS_PROPERTY) + ")) from "
          + VertexLabelConstants.NATURE_RELATIONSHIP
          + ") where in('klass_nature_relationship_of') contains (code='"
          + UtilClass.getCodeNew(natureKlassNode) + "')";
      Iterable<Vertex> klassRelationshipNatureNodes = graph.command(new OCommandSQL(query))
          .execute();
      for (Vertex klassRelationshipNatureNode : klassRelationshipNatureNodes) {
        Map<String, Object> referencedElementMap = UtilClass
            .getMapFromNode(klassRelationshipNatureNode);
        String klassRelationshipId = UtilClass.getCodeNew(klassRelationshipNatureNode);
        referencedElementMap.put(CommonConstants.ID_PROPERTY, klassRelationshipId);
        referencedElementMap.remove(IReferencedNatureRelationshipModel.RELATIONSHIP_TYPE);
        referencedElements.put(klassRelationshipId, referencedElementMap);
        Iterator<Vertex> natureRelationshipIterator = klassRelationshipNatureNode
            .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
            .iterator();
        Vertex natureRelationshipNode = natureRelationshipIterator.next();
        String natureRelationshipId = UtilClass.getCodeNew(natureRelationshipNode);
        Map<String, Object> relationshipMap = RelationshipUtils
            .getRelationshipMapWithContext(natureRelationshipNode);
        List<String> contextTagIds = ConfigDetailsUtils.getContextTagIds(natureRelationshipNode,
            referencedTagsMap);
        relationshipMap.put(IReferencedNatureRelationshipModel.CONTEXT_TAGS, contextTagIds);
        referencedNatureRelationshipMap.put(natureRelationshipId, relationshipMap);
        referencedRelationshipsMapping.put(natureRelationshipId, natureKlassId);
      }
    }
  }
  
  private void fillRelationships(Set<Vertex> klassNodes, Map<String, Object> mapToReturn)
      throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    
    Map<String, Object> referencedRelationshipMap = (Map<String, Object>) mapToReturn
        .get(IGetMultiClassificationKlassDetailsModel.REFERENCED_RELATIONSHIPS);
    Map<String, String> referencedRelationshipsMapping = (Map<String, String>) mapToReturn
        .get(IGetMultiClassificationKlassDetailsModel.REFERENCED_RELATIONSHIPS_MAPPING);
    Map<String, Object> referencedElements = (Map<String, Object>) mapToReturn
        .get(IGetMultiClassificationKlassDetailsModel.REFERENCED_ELEMENTS);
    for (Vertex klassNode : klassNodes) {
      
      String query = "select from (select expand(in('has_property')) from "
          + VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP
          + ") where in('has_klass_property') contains (code='" + UtilClass.getCodeNew(klassNode)
          + "')";
      Iterable<Vertex> iterable = graph.command(new OCommandSQL(query))
          .execute();
      for (Vertex kRNode : iterable) {
        Map<String, Object> referencedElementMap = UtilClass.getMapFromNode(kRNode);
        String klassRelationshipId = UtilClass.getCodeNew(kRNode);
        referencedElementMap.put(CommonConstants.ID_PROPERTY, klassRelationshipId);
        referencedElementMap.remove(IReferencedNatureRelationshipModel.RELATIONSHIP_TYPE);
        referencedElements.put(klassRelationshipId, referencedElementMap);
        Iterator<Vertex> relationshipIterator = kRNode
            .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
            .iterator();
        Vertex relationshipNode = relationshipIterator.next();
        String relationshipId = UtilClass.getCodeNew(relationshipNode);
        Map<String, Object> relationshipMap = RelationshipUtils
            .getRelationshipMapWithContext(relationshipNode);
        referencedRelationshipMap.put(relationshipId, relationshipMap);
        referencedRelationshipsMapping.put(relationshipId, UtilClass.getCodeNew(klassNode));
      }
    }
  }
  
  private void fillTaxonomyDetails(Map<String, Object> mapToReturn, List<String> taxonomyIds,
      IGetConfigDetailsHelperModelForComparingInstances helperModel) throws Exception
  {
    Map<String, Object> referencedTaxonomyMap = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAXONOMIES);
    Set<Vertex> taxonomyVertices = new HashSet<>();
    for (String taxonomyId : taxonomyIds) {
      Vertex taxonomyVertex = null;
      try {
        taxonomyVertex = UtilClass.getVertexByIndexedId(taxonomyId,
            VertexLabelConstants.ROOT_KLASS_TAXONOMY);
        taxonomyVertices.add(taxonomyVertex);
      }
      catch (NotFoundException e) {
        throw new KlassTaxonomyNotFoundException();
      }
      Map<String, Object> taxonomyMap = UtilClass.getMapFromVertex(
          Arrays.asList(CommonConstants.CODE_PROPERTY, IReferencedArticleTaxonomyModel.LABEL,
              IReferencedArticleTaxonomyModel.ICON, IReferencedArticleTaxonomyModel.TAXONOMY_TYPE,
              IReferencedArticleTaxonomyModel.CODE),
          taxonomyVertex);
      
      fillTaxonomiesChildrenAndParentData(taxonomyMap, taxonomyVertex);
      referencedTaxonomyMap.put(taxonomyId, taxonomyMap);
    }
    helperModel.setTaxonomyVertices(taxonomyVertices);
  }
  
  protected String fillTaxonomiesChildrenAndParentData(Map<String, Object> taxonomyMap,
      Vertex taxonomyVertex) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    String rid = taxonomyVertex.getId()
        .toString();
    List<Map<String, Object>> taxonomiesChildrenList = new ArrayList<>();
    Map<String, Object> taxonomiesParentMap = new HashMap<>();
    
    List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY, ITaxonomy.LABEL,
        ITaxonomy.ICON, IReferencedArticleTaxonomyModel.TAXONOMY_TYPE,
        IReferencedArticleTaxonomyModel.CODE);
    String query = "select from(traverse in('Child_Of') from " + rid
        + " strategy BREADTH_FIRST) where code <> '" + UtilClass.getCodeNew(taxonomyVertex) + "'";
    
    Iterable<Vertex> resultIterable = graph.command(new OCommandSQL(query))
        .execute();
    for (Vertex childNode : resultIterable) {
      taxonomiesChildrenList.add(UtilClass.getMapFromVertex(fieldsToFetch, childNode));
    }
    
    taxonomyMap.put(IReferencedArticleTaxonomyModel.CHILDREN, taxonomiesChildrenList);
    taxonomyMap.put(IReferencedArticleTaxonomyModel.PARENT, taxonomiesParentMap);
    return MultiClassificationUtils.fillParentsData(fieldsToFetch, taxonomiesParentMap,
        taxonomyVertex);
  }
  
  /*protected String fillParentsData(List<String> fieldsToFetch, Map<String, Object> taxonomiesParentMap, Vertex taxonomyVertex)
  {
    Iterator<Vertex> parentNodes = taxonomyVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF).iterator();
    String parentId = null;
    if(!parentNodes.hasNext()){
      taxonomiesParentMap.put(IReferencedTaxonomyParentModel.ID, "-1");
      parentId =  UtilClass.getCode(taxonomyVertex);
    }
  
    while(parentNodes.hasNext()) {
      Vertex vertex = parentNodes.next();
      Map<String,Object> parentMap = new HashMap<>();
      taxonomiesParentMap.putAll(UtilClass.getMapFromVertex(fieldsToFetch, vertex));
      taxonomiesParentMap.put(IReferencedTaxonomyParentModel.PARENT,parentMap);
      parentId = fillParentsData(fieldsToFetch,parentMap,vertex);
    }
    return parentId;
  }*/
  
  private void fillReferenceRelationshipVariantContext(Map<String, Object> mapToReturn)
      throws Exception
  {
    Set<String> contextIds = new HashSet<String>();
    Map<String, Object> referencedRelationshipMap = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_RELATIONSHIPS);
    
    for (Object relationship : referencedRelationshipMap.values()) {
      Map<String, Object> relationshipMap = (Map<String, Object>) relationship;
      Map<String, Object> side1Map = (Map<String, Object>) relationshipMap.get(IRelationship.SIDE1);
      Map<String, Object> side2Map = (Map<String, Object>) relationshipMap.get(IRelationship.SIDE2);
      String side1ContextId = (String) side1Map.get(IRelationshipSide.CONTEXT_ID);
      String side2ContextId = (String) side2Map.get(IRelationshipSide.CONTEXT_ID);
      if (side1ContextId != null) {
        contextIds.add(side1ContextId);
      }
      if (side2ContextId != null) {
        contextIds.add(side2ContextId);
      }
    }
    Map<String, Object> referencedVariantContexts = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_VARIANT_CONTEXTS);
    Map<String, Object> relationshipVariantContexts = (Map<String, Object>) referencedVariantContexts
        .get(IReferencedContextModel.RELATIONSHIP_VARIANT_CONTEXTS);
    Map<String, Object> referencedTagsMap = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAGS);
    for (String contextId : contextIds) {
      Vertex contextNode = UtilClass.getVertexById(contextId, VertexLabelConstants.VARIANT_CONTEXT);
      Map<String, Object> contextMap = VariantContextUtils.getReferencedContexts(contextNode);
      relationshipVariantContexts.put(contextId, contextMap);
      List<Map<String, Object>> contextTags = (List<Map<String, Object>>) contextMap
          .get(IReferencedVariantContextModel.TAGS);
      
      for (Map<String, Object> tag : contextTags) {
        String tagId = (String) tag.get(IReferencedVariantContextTagsModel.TAG_ID);
        if (referencedTagsMap.containsKey(tagId)) {
          continue;
        }
        Vertex tagNode = UtilClass.getVertexById(tagId, VertexLabelConstants.ENTITY_TAG);
        Map<String, Object> entity = TagUtils.getTagMap(tagNode, false);
        referencedTagsMap.put(tagId, entity);
      }
    }
  }
  
  private Map<String, Object> getMapToReturn()
  {
    Map<String, Object> mapToReturn = new HashMap<>();
    Map<String, Object> referencedKlassMap = new HashMap<>();
    Map<String, Object> referencedAttributeMap = new HashMap<>();
    Map<String, Object> referencedTagMap = new HashMap<>();
    Map<String, Object> referencedTaxonomies = new HashMap<>();
    List<String> referencedLifeCycleStatusTags = new ArrayList<>();
    Map<String, Object> referencedRoleMap = new HashMap<>();
    Map<String, Object> referencedNatureRelationships = new HashMap<>();
    Map<String, String> referencedRelationshipsMapping = new HashMap<>();
    Map<String, Object> referencedRelationshipMap = new HashMap<>();
    Map<String, Object> referencedVariantContextsMap = new HashMap<>();
    Map<String, Object> relationshipVariantContexts = new HashMap<>();
    referencedVariantContextsMap.put(IReferencedContextModel.RELATIONSHIP_VARIANT_CONTEXTS,
        relationshipVariantContexts);
    mapToReturn.put(IGetConfigDetailsModel.REFERENCED_KLASSES, referencedKlassMap);
    mapToReturn.put(IGetConfigDetailsModel.REFERENCED_ATTRIBUTES, referencedAttributeMap);
    mapToReturn.put(IGetConfigDetailsModel.REFERENCED_TAGS, referencedTagMap);
    mapToReturn.put(IGetConfigDetailsModel.REFERENCED_TAXONOMIES, referencedTaxonomies);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_ROLES, referencedRoleMap);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_RELATIONSHIPS,
        referencedRelationshipMap);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_NATURE_RELATIONSHIPS,
        referencedNatureRelationships);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_RELATIONSHIPS_MAPPING,
        referencedRelationshipsMapping);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_NATURE_RELATIONSHIPS,
        referencedNatureRelationships);
    mapToReturn.put(IGetConfigDetailsModel.REFERENCED_LIFECYCLE_STATUS_TAGS,
        referencedLifeCycleStatusTags);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_VARIANT_CONTEXTS,
        referencedVariantContextsMap);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_ELEMENTS, new HashMap<>());
    mapToReturn.put(IGetConfigDetailsModel.NUMBER_OF_VERSIONS_TO_MAINTAIN, 1);
    return mapToReturn;
  }
}
