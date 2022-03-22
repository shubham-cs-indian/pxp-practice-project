package com.cs.runtime.strategy.plugin.usecase.base;

import com.cs.config.standard.IStandardConfig;
import com.cs.config.strategy.plugin.model.IGetConfigDetailsHelperModel;
import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.config.strategy.plugin.usecase.relationship.util.GetRelationshipUtils;
import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.task.util.TasksUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variantcontext.util.VariantContextUtils;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.IKlassNatureRelationship;
import com.cs.core.config.interactor.entity.relationship.IReferencedRelationshipProperty;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.task.ITask;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.config.interactor.exception.staticcollection.CollectionNodeNotFoundException;
import com.cs.core.config.interactor.model.globalpermissions.IReferencedTemplatePermissionModel;
import com.cs.core.config.interactor.model.klass.IReferencedNatureRelationshipModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionRelationshipModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedTaxonomyParentModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextTagsModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipPropertiesModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.repository.relationship.RelationshipRepository;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@SuppressWarnings("unchecked")
public abstract class AbstractConfigDetailsForCustomTab extends AbstractGetConfigDetails {
  
  public AbstractConfigDetailsForCustomTab(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  protected void fillCollectionDetails(Map<String, Object> mapToReturn, List<String> collectionIds,
      IGetConfigDetailsHelperModel helperModel, String requestedTypeId) throws Exception
  {
    Set<Vertex> collectionVertices = helperModel.getCollectionVertices();
    for (String collectionId : collectionIds) {
      try {
        Vertex collectionVertex = UtilClass.getVertexById(collectionId,
            VertexLabelConstants.COLLECTION);
        if (collectionId.equals(requestedTypeId)) {
          helperModel.setRequestedTypeVertex(collectionVertex);
          helperModel.setRequestedTypeVertexLabelInfo(VertexLabelConstants.COLLECTION);
        }
        collectionVertices.add(collectionVertex);
        fillReferencedElements(mapToReturn, collectionVertex, VertexLabelConstants.COLLECTION, null,
            helperModel, null);
        fillReferencedPropertyCollections(helperModel, collectionVertex, mapToReturn, null);
      }
      catch (NotFoundException ex) {
        throw new CollectionNodeNotFoundException(ex);
      }
    }
  }
  
  protected void fillKlassDetails(Map<String, Object> mapToReturn, List<String> klassIds,
      Map<String, Object> referencedDataRuleMap, IGetConfigDetailsHelperModel helperModel,
      String requestedTypeId, String roleId) throws Exception
  {
    Map<String, Object> referencedKlassMap = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_KLASSES);
    List<Map<String, Object>> referencedTemplates = (List<Map<String, Object>>) mapToReturn
        .get(IGetConfigDetailsModel.REFERENCED_TEMPLATES);
    Map<String, Object> referencedPermissions = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsModel.REFERENCED_PERMISSIONS);

    Iterable<Vertex> klassVertices = UtilClass.getVerticesByIndexedIds(klassIds, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    for (Vertex klassVertex : klassVertices) {
       String klassId = UtilClass.getCode(klassVertex);
        if (klassId.equals(requestedTypeId)) {
          helperModel.setRequestedTypeVertex(klassVertex);
          helperModel.setRequestedTypeVertexLabelInfo(VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
        }
        List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
            IKlass.NUMBER_OF_VERSIONS_TO_MAINTAIN, IKlass.ICON, IKlass.LABEL, IKlass.TYPE,
            IKlass.NATURE_TYPE, IKlass.IS_NATURE, IKlass.PREVIEW_IMAGE, IKlass.CODE,
            IKlass.CLASSIFIER_IID);
        
        Map<String, Object> klassMap = UtilClass.getMapFromVertex(fieldsToFetch, klassVertex);
        helperModel.setKlassType(klassVertex.getProperty(IKlass.TYPE));
        fillNumberOfVersionsToMaintain(mapToReturn, klassMap);
        fillReferencedTagsAndLifeCycleStatusTags(mapToReturn, klassVertex);
        fillDataRulesOfKlass(klassVertex, referencedDataRuleMap, helperModel,
            RelationshipLabelConstants.HAS_KLASS_RULE_LINK);
        fillReferencedTasks(klassVertex, mapToReturn, roleId);
        fillReferencedElements(mapToReturn, klassVertex,
            VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS, null, helperModel, null);
        fillReferencedPropertyCollections(helperModel, klassVertex, mapToReturn, null);
        fillEmbeddedReferencedContext(klassVertex, mapToReturn, helperModel);
        klassMap.put(IReferencedArticleTaxonomyModel.PROPERTY_COLLECTIONS,
            UtilClass.getCodes(
                new ArrayList<>(helperModel.getTypeIdVsAssociatedPropertyCollectionVertices()
                    .get(klassId))));
        Boolean isNature = klassVertex.getProperty(IKlass.IS_NATURE);
        if (isNature != null && isNature) {
          helperModel.setNatureNode(klassVertex);
          fillReferencedNatureRelationships(mapToReturn, klassVertex, helperModel);
        }
        else {
          helperModel.getNonNatureKlassNodes().add(klassVertex);
        }
      referencedKlassMap.put(klassId, klassMap);
      fillReferencedTemplates(referencedTemplates, roleId, klassId, referencedPermissions);

    }
  }

  protected void fillNumberOfVersionsToMaintain(Map<String, Object> mapToReturn, Map<String, Object> klassMap)
  {
    Integer numberOfVersionsToMaintain = (Integer) klassMap
        .get(IKlass.NUMBER_OF_VERSIONS_TO_MAINTAIN);
    Integer existingNumberOfVersionsToMaintain = (Integer) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.NUMBER_OF_VERSIONS_TO_MAINTAIN);
    if (numberOfVersionsToMaintain > existingNumberOfVersionsToMaintain) {
      mapToReturn.put(IGetConfigDetailsForCustomTabModel.NUMBER_OF_VERSIONS_TO_MAINTAIN,
          numberOfVersionsToMaintain);
    }
  }
  
  protected void fillReferencedNatureRelationships(Map<String, Object> mapToReturn,
      Vertex klassNode, IGetConfigDetailsHelperModel helperModel) throws Exception
  {
    Map<String, Object> referencedNatureRelationshipMap = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_NATURE_RELATIONSHIPS);
    Map<String, String> referencedRelationshipsMapping = (Map<String, String>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_RELATIONSHIPS_MAPPING);
    Map<String, Object> referencedRelationshipProperties = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_RELATIONSHIPS_PROPERTIES);
    Map<String, Object> referencedElementMap = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_ELEMENTS);
    Map<String, Object> referencedTagsMap = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAGS);
    
    Iterable<Edge> side2klassNatureRelationshipOfEdges = RelationshipRepository
        .getSide2KREdgesOfNatureRelationshipFromSide1klassId(klassNode.getId());
    Iterable<Edge> klassNatureRelationshipOfEdges = klassNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF);
    
    String klassId = UtilClass.getCodeNew(klassNode);
    
    Stream<Edge> klassNatureRelationshipOfEdgesStream = Stream.concat(
        StreamSupport.stream(side2klassNatureRelationshipOfEdges.spliterator(), false),
        StreamSupport.stream(klassNatureRelationshipOfEdges.spliterator(), false));
    Iterable<Edge> klassNatureRelationshipOfEdgesIterable = () -> klassNatureRelationshipOfEdgesStream
        .iterator();
    
    Map<String, Set<Vertex>> typeIdVsAssociatedRelationshipsVertices = helperModel
        .getTypeIdVsAssociatedRelationshipsVertices();
    Map<String, Set<String>> typeIdVsAssociatedPropertyIds = helperModel
        .getTypeIdVsAssociatedPropertyIds();
    Set<Vertex> associatedRelationshipVertices = new HashSet<>();
    if (typeIdVsAssociatedRelationshipsVertices.containsKey(klassId)) {
      associatedRelationshipVertices = typeIdVsAssociatedRelationshipsVertices.get(klassId);
    }
    else {
      typeIdVsAssociatedRelationshipsVertices.put(klassId, associatedRelationshipVertices);
    }
    
    for (Edge klassNatureRelationshipOfEdge : klassNatureRelationshipOfEdgesIterable) {
      Vertex klassNatureRelationshipVertex = klassNatureRelationshipOfEdge.getVertex(Direction.IN);
      Iterable<Vertex> natureRelationshipNodes = klassNatureRelationshipVertex
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY);
      for (Vertex natureRelationshipNode : natureRelationshipNodes) {
        
        associatedRelationshipVertices.add(natureRelationshipNode);
        String relationshipType = natureRelationshipNode
            .getProperty(IKlassNatureRelationship.RELATIONSHIP_TYPE);
        
        String natureRelationshipId = UtilClass.getCodeNew(natureRelationshipNode);
        
        // Adding Referenced Element
        Map<String, Object> referencedElement = UtilClass.getMapFromVertex(new ArrayList<>(),
            klassNatureRelationshipVertex);
        String natureType = (String) referencedElement
            .remove(IReferencedNatureRelationshipModel.NATURE_TYPE);
        String klassNatureRelationshipId = UtilClass.getCodeNew(klassNatureRelationshipVertex);
        helperModel.getKlassNatureRelationshipIds()
            .add(klassNatureRelationshipId);
        referencedElement.remove(IReferencedNatureRelationshipModel.RELATIONSHIP_TYPE);
        referencedElement.put(IReferencedSectionRelationshipModel.ID, klassNatureRelationshipId);
        referencedElement.put(IReferencedSectionRelationshipModel.IS_DISABLED, false);
        if (typeIdVsAssociatedPropertyIds.containsKey(klassId)) {
          Set<String> associatedPropertyIds = typeIdVsAssociatedPropertyIds.get(klassId);
          associatedPropertyIds.add(natureRelationshipId);
        }
        else {
          Set<String> associatedPropertyIds = new HashSet<>();
          associatedPropertyIds.add(natureRelationshipId);
          typeIdVsAssociatedPropertyIds.put(klassId, associatedPropertyIds);
        }
        Map<String, Object> side = (Map<String, Object>) referencedElement
            .get(IReferencedSectionRelationshipModel.RELATIONSHIP_SIDE);
        
        GetRelationshipUtils.prepareSideMapTranslation(side);
        
        // Adding Referenced Nature Relationship
        Map<String, Object> relationshipMap = RelationshipUtils
            .getNatureRelationshipMap(natureRelationshipNode);
        relationshipMap.put(IReferencedNatureRelationshipModel.NATURE_TYPE, natureType);
        if (side != null) {
          side.put(CommonConstants.RELATIONSHIP_MAPPING_ID_PROPERTY, klassNatureRelationshipId);
          String contextId = RelationshipUtils
              .getContextIdForNatureRelationship(klassNatureRelationshipVertex);
          side.put(IRelationshipSide.CONTEXT_ID, contextId);
        }
        
        List<String> contextTagIds = getContextTagIds(natureRelationshipNode, referencedTagsMap);
        helperModel.getContextTagIds()
            .addAll(contextTagIds);
        relationshipMap.put(IReferencedNatureRelationshipModel.CONTEXT_TAGS, contextTagIds);
        
        referencedNatureRelationshipMap.put(natureRelationshipId, relationshipMap);
        helperModel.getNatureRelationshipIds()
            .add(natureRelationshipId);
        referencedElementMap.put(klassNatureRelationshipId, referencedElement);
        referencedRelationshipsMapping.put(natureRelationshipId, klassId);
        String label = (String) UtilClass.getValueByLanguage(natureRelationshipNode,
            CommonConstants.LABEL_PROPERTY);
        Map<String, Object> relationshipPropertiesMap = new HashMap<>();
        
        relationshipPropertiesMap.put(IReferencedRelationshipPropertiesModel.ID,
            natureRelationshipId);
        relationshipPropertiesMap.put(IReferencedRelationshipPropertiesModel.LABEL, label);
        relationshipPropertiesMap.put(IReferencedRelationshipPropertiesModel.CODE,
            (String) natureRelationshipNode.getProperty(IReferencedRelationshipProperty.CODE));
        
        RelationshipUtils.populatePropetiesInfoNew(natureRelationshipNode,
            relationshipPropertiesMap);
        referencedRelationshipProperties.put(natureRelationshipId, relationshipPropertiesMap);
        Map<String, Object> referencedVariantContextMap = (Map<String, Object>) mapToReturn
            .get(IGetConfigDetailsForCustomTabModel.REFERENCED_VARIANT_CONTEXTS);
        if (relationshipType != null
            && relationshipType.equals(CommonConstants.PRODUCT_VARIANT_RELATIONSHIP)) {
          fillVariantContextsOfKlass(klassNatureRelationshipVertex, referencedVariantContextMap,
              mapToReturn, helperModel.getContextTagIds(),
              IReferencedContextModel.PRODUCT_VARIANT_CONTEXTS);
        }
      }
    }
  }
  
  
  private List<String> getContextTagIds(Vertex natureRelationshipNode,
      Map<String, Object> referencedTagsMap) throws Exception
  {
    List<String> contextTagIds = new ArrayList<>();
    Iterable<Vertex> contextTagVertices = natureRelationshipNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_RELATIONSHIP_CONTEXT_TAG);
    for (Vertex contextTagVertex : contextTagVertices) {
      String contextTagId = UtilClass.getCodeNew(contextTagVertex);
      contextTagIds.add(contextTagId);
      if (!referencedTagsMap.containsKey(contextTagId)) {
        Map<String, Object> referencedTag = TagUtils.getTagMap(contextTagVertex, true);
        referencedTagsMap.put(contextTagId, referencedTag);
      }
    }
    return contextTagIds;
  }
  
  protected void fillTaxonomyDetails(Map<String, Object> mapToReturn, List<String> taxonomyIds,
      Map<String, Object> referencedDataRuleMap, IGetConfigDetailsHelperModel helperModel,
      String requestedTypeId) throws Exception
  {
    Map<String, Object> referencedTaxonomyMap = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAXONOMIES);
    Iterable<Vertex> taxonomyVertices = UtilClass.getVerticesByIndexedIds(taxonomyIds, VertexLabelConstants.ROOT_KLASS_TAXONOMY);
    for (Vertex taxonomyVertex : taxonomyVertices) {
      String taxonomyId = UtilClass.getCode(taxonomyVertex);

      if (taxonomyId.equals(requestedTypeId)) {
        helperModel.setRequestedTypeVertex(taxonomyVertex);
        helperModel.setRequestedTypeVertexLabelInfo(VertexLabelConstants.ROOT_KLASS_TAXONOMY);
      }

      helperModel.getTaxonomyVertices().add(taxonomyVertex);

      Map<String, Object> taxonomyMap = UtilClass.getMapFromVertex(
          Arrays.asList(CommonConstants.CODE_PROPERTY, IReferencedArticleTaxonomyModel.LABEL,
              IReferencedArticleTaxonomyModel.ICON, IReferencedArticleTaxonomyModel.TAXONOMY_TYPE,
              IReferencedArticleTaxonomyModel.CODE, IReferencedArticleTaxonomyModel.BASETYPE),
          taxonomyVertex);
      
      fillTaxonomiesChildrenAndParentData(taxonomyMap, taxonomyVertex);
      fillReferencedTasks(taxonomyVertex, mapToReturn);
      fillDataRulesOfKlass(taxonomyVertex, referencedDataRuleMap, helperModel,
          RelationshipLabelConstants.HAS_TAXONOMY_RULE_LINK);
      fillReferencedElements(mapToReturn, taxonomyVertex, VertexLabelConstants.ROOT_KLASS_TAXONOMY,
          null, helperModel, null);
      fillReferencedPropertyCollections(helperModel, taxonomyVertex, mapToReturn, null);
      taxonomyMap.put(IReferencedArticleTaxonomyModel.PROPERTY_COLLECTIONS,
          UtilClass.getCodes(
              new ArrayList<>(helperModel.getTypeIdVsAssociatedPropertyCollectionVertices()
                  .get(taxonomyId))));
      fillEmbeddedReferencedContext(taxonomyVertex, mapToReturn, helperModel);
      referencedTaxonomyMap.put(taxonomyId, taxonomyMap);
    }
  }
  
  protected void fillEmbeddedReferencedContext(Vertex klassVertex, Map<String, Object> mapToReturn,
      IGetConfigDetailsHelperModel helperModel) throws Exception
  {
    String klassType = helperModel.getKlassType();
    Map<String, Object> referencedVariantContexts = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_VARIANT_CONTEXTS);
    Map<String, Object> embeddedContexts = (Map<String, Object>) referencedVariantContexts
        .get(IReferencedContextModel.EMBEDDED_VARIANT_CONTEXTS);
    Map<String, Object> referencedKlasses = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_KLASSES);
    List<Map<String, Object>> contextsWithAutoCreateEnabled = (List<Map<String, Object>>) mapToReturn
        .get(
            IGetConfigDetailsForCustomTabModel.TECHNICAL_IMAGE_VARIANT_CONTEXT_WITH_AUTO_CREATE_ENABLE);
    
    List<String> embeddedContextIds = new ArrayList<>();
    Set<Vertex> associatedContextVertices = new HashSet<>();
    Map<String, Set<Vertex>> typeIdVsAssociatedContextVertices = helperModel
        .getTypeIdVsAssociatedContextVertices();
    String klassId = UtilClass.getCodeNew(klassVertex);
    if (typeIdVsAssociatedContextVertices.containsKey(klassId)) {
      associatedContextVertices = typeIdVsAssociatedContextVertices.get(klassId);
    }
    else {
      typeIdVsAssociatedContextVertices.put(klassId, associatedContextVertices);
    }
    Iterable<Vertex> contextKlassIterable = klassVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_CONTEXT_KLASS);
    for (Vertex contextKlassNode : contextKlassIterable) {
      String contextKlassType = contextKlassNode.getProperty(IKlass.TYPE);
      if (!contextKlassType.equals(klassType)) {
        continue;
      }
      Iterator<Vertex> variantContextsIterator = contextKlassNode
          .getVertices(Direction.OUT, RelationshipLabelConstants.VARIANT_CONTEXT_OF)
          .iterator();
      Vertex variantContextNode = variantContextsIterator.next();
      associatedContextVertices.add(variantContextNode);
      String contextKlassId = UtilClass.getCodeNew(contextKlassNode);
      String contextId = UtilClass.getCodeNew(variantContextNode);
      helperModel.getKlassIdVsContextId()
          .put(contextKlassId, contextId);
      embeddedContextIds.add(contextId);
      
      Map<String, Object> variantContextMap = VariantContextUtils
          .getReferencedContexts(variantContextNode);
      
      Map<String, Object> contextWithAutoCreateEnable = KlassGetUtils
          .getContextMapWithAutoCreateEnabled(variantContextNode);
      if (contextWithAutoCreateEnable != null) {
        contextsWithAutoCreateEnabled.add(contextWithAutoCreateEnable);
      }
      
      List<String> entityIds = new ArrayList<>();
      entityIds.add(contextKlassId);
      variantContextMap.put(IReferencedVariantContextModel.ENTITY_IDS, entityIds);
      embeddedContexts.put(contextId, variantContextMap);
      
      // fill context klass in referencedKlass
      variantContextMap.put(IReferencedVariantContextModel.CONTEXT_KLASS_ID, contextKlassId);
      List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
          IKlass.NUMBER_OF_VERSIONS_TO_MAINTAIN, IKlass.ICON, IKlass.LABEL, IKlass.TYPE,
          IKlass.NATURE_TYPE, IKlass.IS_NATURE, IKlass.CODE);
      Map<String, Object> klassMap = UtilClass.getMapFromVertex(fieldsToFetch, contextKlassNode);
      referencedKlasses.put(contextKlassId, klassMap);
    }
  }
  
  protected void fillReferencedContextLinkedToKlass(Map<String, Object> embeddedContexts,
      Map<String, Object> referencedTags,
      IGetConfigDetailsHelperModel helperModel) throws Exception
  {
    Set<Vertex> associatedContextVertices = new HashSet<>();
    Map<String, Set<Vertex>> typeIdVsAssociatedContextVertices = helperModel
        .getTypeIdVsAssociatedContextVertices();
    Vertex klassVertex = helperModel.getNatureNode();
    String klassId = UtilClass.getCodeNew(klassVertex);
    if (typeIdVsAssociatedContextVertices.containsKey(klassId)) {
      associatedContextVertices = typeIdVsAssociatedContextVertices.get(klassId);
    }
    else {
      typeIdVsAssociatedContextVertices.put(klassId, associatedContextVertices);
    }
    Set<String> contextTagIds = helperModel.getContextTagIds();
    
    Iterator<Vertex> iterator = klassVertex
        .getVertices(Direction.OUT, RelationshipLabelConstants.VARIANT_CONTEXT_OF)
        .iterator();
    if (iterator.hasNext()) {
      Vertex variantContextVertex = iterator.next();
      associatedContextVertices.add(variantContextVertex);
      String contextId = UtilClass.getCodeNew(variantContextVertex);
      String variantType = (String) variantContextVertex.getProperty(IVariantContext.TYPE);
      switch (variantType) {
        case CommonConstants.CONTEXTUAL_VARIANT:
        case CommonConstants.LANGUAGE_VARIANT:
        case CommonConstants.GTIN_VARIANT:
        case CommonConstants.IMAGE_VARIANT:
        case CommonConstants.PROMOTION_CONTEXT:
          Map<String, Object> variantContextMap = VariantContextUtils
              .getReferencedContexts(variantContextVertex);
          String contextKlassId = klassId;
          variantContextMap.put(IReferencedVariantContextModel.CONTEXT_KLASS_ID, contextKlassId);
          List<String> entityIds = new ArrayList<>();
          entityIds.add(contextKlassId);
          variantContextMap.put(IReferencedVariantContextModel.ENTITY_IDS, entityIds);
          
          embeddedContexts.put(contextId, variantContextMap);
          
          for (Map<String, Object> contextTag : (List<Map<String, Object>>) variantContextMap
              .get(IReferencedVariantContextModel.TAGS)) {
            String entityId = (String) contextTag.get(IReferencedVariantContextTagsModel.TAG_ID);
            Vertex entityVertex = UtilClass.getVertexById(entityId,
                VertexLabelConstants.ENTITY_TAG);
            Map<String, Object> entity = TagUtils.getTagMap(entityVertex, false);
            // List<String> tagValuesIds = (List<String>)
            // contextTag.get(IReferencedVariantContextTagsModel.TAG_VALUE_IDS);
            // filterChildrenTagsInKlass(entity, tagValuesIds, (Map<String,
            // Object>) referencedTags.get(entityId));
            referencedTags.put(entityId, entity);
            contextTagIds.add(entityId);
          }
          break;
      }
    }
  }
  
  protected void fillTabIdsAssociatedWithRequestedType(IGetConfigDetailsHelperModel helperModel,
      Set<String> tabIds, Map<String, Object> mapToReturn)
  {
    Map<String, Set<Vertex>> typeIdVsAssociatedContextVertices = helperModel
        .getTypeIdVsAssociatedContextVertices();
    Map<String, Set<Vertex>> typeIdVsAssociatedRelationshipVertices = helperModel
        .getTypeIdVsAssociatedRelationshipsVertices();
    Map<String, Set<Vertex>> typeIdVsAssociatedPropertyCollectionVertices = helperModel
        .getTypeIdVsAssociatedPropertyCollectionVertices();
    Vertex requestedTypeVertex = helperModel.getRequestedTypeVertex();
    String requestedTypeVertexLabelInfo = helperModel.getRequestedTypeVertexLabelInfo();
    String requestedTypeId = UtilClass.getCodeNew(requestedTypeVertex);
    Set<Vertex> associatedPropertyCollectionVertices = new HashSet<>(
        typeIdVsAssociatedPropertyCollectionVertices.get(requestedTypeId));
    if (requestedTypeVertexLabelInfo.equals(VertexLabelConstants.ROOT_KLASS_TAXONOMY)) {
      Map<String, Object> referencedTaxonomies = (Map<String, Object>) mapToReturn
          .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAXONOMIES);
      if (referencedTaxonomies.containsKey(requestedTypeId)) {
        Map<String, Object> referencedTaxonomy = (Map<String, Object>) referencedTaxonomies
            .get(requestedTypeId);
        Map<String, Object> rootLevelParent = new HashMap<>(
            (Map<String, Object>) referencedTaxonomy);
        Map<String, Object> parent = (Map<String, Object>) (rootLevelParent)
            .get(IReferencedArticleTaxonomyModel.PARENT);
        while (!(parent.get(IReferencedTaxonomyParentModel.ID)).equals("-1")) {
          rootLevelParent.clear();
          rootLevelParent.putAll(parent);
          parent = (Map<String, Object>) (parent).get(IReferencedArticleTaxonomyModel.PARENT);
        }
        String taxonomyType = (String) rootLevelParent
            .get(IReferencedArticleTaxonomyModel.TAXONOMY_TYPE);
        if (taxonomyType.equals(CommonConstants.MINOR_TAXONOMY)) {
          associatedPropertyCollectionVertices.clear();
        }
      }
    }
    Set<Vertex> associatedPropertyCollectionVerticesWithReadPermission = getAssociatedPropertyCollectionVerticesWithReadPermission(
        associatedPropertyCollectionVertices, mapToReturn);
    fillTabIdsAssociatedWithPropertyCollection(
        associatedPropertyCollectionVerticesWithReadPermission, tabIds);
    if (!requestedTypeVertexLabelInfo.equals(VertexLabelConstants.COLLECTION)) {
      Set<Vertex> associatedContextVertices = typeIdVsAssociatedContextVertices
          .get(requestedTypeId);
      Set<Vertex> associatedContextVerticesWithReadPermission = getAssociatedContextVerticesWithReadPermission(
          associatedContextVertices, helperModel);
      fillTabIdsAssociatedWithContexts(associatedContextVerticesWithReadPermission, tabIds);
      Set<Vertex> associatedRelationshipVertices = typeIdVsAssociatedRelationshipVertices
          .get(requestedTypeId);
      Set<Vertex> associatedRelationshipVerticesWithReadPermission = getAssociatedRelationshipVerticesWithReadPermission(
          associatedRelationshipVertices, mapToReturn, helperModel.getNatureRelationshipIds());
      fillTabIdsAssociatedWithRelationships(associatedRelationshipVerticesWithReadPermission,
          tabIds);
      fillAssociatedSectionIds(new HashSet<>(), associatedContextVertices,
          associatedRelationshipVertices, helperModel);
    }
    fillAssociatedSectionIds(associatedPropertyCollectionVertices, new HashSet<>(),
        new HashSet<>(), helperModel);
  }
  
  protected void fillTabsIdsAssociatedCollections(IGetConfigDetailsHelperModel helperModel,
      Set<String> tabIds, Map<String, Object> mapToReturn)
  {
    Map<String, Set<Vertex>> typeIdVsAssociatedPropertyCollectionVertices = helperModel
        .getTypeIdVsAssociatedPropertyCollectionVertices();
    Set<Vertex> collectionVertices = helperModel.getCollectionVertices();
    for (Vertex collectionVertex : collectionVertices) {
      String collectionId = UtilClass.getCodeNew(collectionVertex);
      Set<Vertex> associatedPropertyCollectionVertices = typeIdVsAssociatedPropertyCollectionVertices
          .get(collectionId);
      Set<Vertex> associatedPropertyCollectionVerticesWithReadPermission = getAssociatedPropertyCollectionVerticesWithReadPermission(
          associatedPropertyCollectionVertices, mapToReturn);
      fillTabIdsAssociatedWithPropertyCollection(
          associatedPropertyCollectionVerticesWithReadPermission, tabIds);
      fillAssociatedSectionIds(associatedPropertyCollectionVertices, new HashSet<>(),
          new HashSet<>(), helperModel);
    }
  }
  
  protected void fillTabIdsAssociatedWithNonNatureKlasses(IGetConfigDetailsHelperModel helperModel,
      Set<String> tabIds, Map<String, Object> mapToReturn)
  {
    Map<String, Set<Vertex>> typeIdVsAssociatedContextVertices = helperModel
        .getTypeIdVsAssociatedContextVertices();
    Map<String, Set<Vertex>> typeIdVsAssociatedRelationshipVertices = helperModel
        .getTypeIdVsAssociatedRelationshipsVertices();
    Map<String, Set<Vertex>> typeIdVsAssociatedPropertyCollectionVertices = helperModel
        .getTypeIdVsAssociatedPropertyCollectionVertices();
    Set<Vertex> nonNatureKlassVertices = helperModel.getNonNatureKlassNodes();
    for (Vertex nonNatureKlassVertex : nonNatureKlassVertices) {
      String nonNatureKlassId = UtilClass.getCodeNew(nonNatureKlassVertex);
      Set<Vertex> associatedPropertyCollectionVertices = typeIdVsAssociatedPropertyCollectionVertices
          .get(nonNatureKlassId);
      Set<Vertex> associatedPropertyCollectionVerticesWithReadPermission = getAssociatedPropertyCollectionVerticesWithReadPermission(
          associatedPropertyCollectionVertices, mapToReturn);
      fillTabIdsAssociatedWithPropertyCollection(
          associatedPropertyCollectionVerticesWithReadPermission, tabIds);
      Set<Vertex> associatedContextVertices = typeIdVsAssociatedContextVertices
          .get(nonNatureKlassId);
      Set<Vertex> associatedContextVerticesWithReadPermission = getAssociatedContextVerticesWithReadPermission(
          associatedContextVertices, helperModel);
      fillTabIdsAssociatedWithContexts(associatedContextVerticesWithReadPermission, tabIds);
      Set<Vertex> associatedRelationshipVertices = typeIdVsAssociatedRelationshipVertices
          .get(nonNatureKlassId);
      Set<Vertex> associatedRelationshipVerticesWithReadPermission = getAssociatedRelationshipVerticesWithReadPermission(
          associatedRelationshipVertices, mapToReturn, helperModel.getNatureRelationshipIds());
      fillTabIdsAssociatedWithRelationships(associatedRelationshipVerticesWithReadPermission,
          tabIds);
      fillAssociatedSectionIds(associatedPropertyCollectionVertices, associatedContextVertices,
          associatedRelationshipVertices, helperModel);
    }
  }
  
  protected void fillTabIdsAssociatedWithTaxonomies(IGetConfigDetailsHelperModel helperModel,
      Set<String> tabIds, Map<String, Object> mapToReturn)
  {
    Map<String, Set<Vertex>> typeIdVsAssociatedContextVertices = helperModel
        .getTypeIdVsAssociatedContextVertices();
    Map<String, Set<Vertex>> typeIdVsAssociatedRelationshipVertices = helperModel
        .getTypeIdVsAssociatedRelationshipsVertices();
    Map<String, Set<Vertex>> typeIdVsAssociatedPropertyCollectionVertices = helperModel
        .getTypeIdVsAssociatedPropertyCollectionVertices();
    Set<Vertex> taxonomyVertices = helperModel.getTaxonomyVertices();
    for (Vertex taxonomyVertex : taxonomyVertices) {
      String query = "select from (select from(traverse out('"
          + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF + "') from "
          + taxonomyVertex.getId() + "strategy DEPTH_FIRST) where " + ITag.TYPE
          + " is null) where out('" + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF
          + "').size() = 0";
      Iterable<Vertex> rootTaxonomyVertexIterable = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      Iterator<Vertex> rootTaxonomyIterator = rootTaxonomyVertexIterable.iterator();
      Vertex rootTaxonomyVertex = rootTaxonomyIterator.next();
      String taxonomyType = (String) rootTaxonomyVertex.getProperty(ITaxonomy.TAXONOMY_TYPE);
      Boolean isTaxonomyTypeMinor = taxonomyType.equals(CommonConstants.MINOR_TAXONOMY) ? true
          : false;
      String taxonomyId = UtilClass.getCodeNew(taxonomyVertex);
      if (!isTaxonomyTypeMinor) {
        Set<Vertex> associatedPropertyCollectionVertices = typeIdVsAssociatedPropertyCollectionVertices
            .get(taxonomyId);
        Set<Vertex> associatedPropertyCollectionVerticesWithReadPermission = getAssociatedPropertyCollectionVerticesWithReadPermission(
            associatedPropertyCollectionVertices, mapToReturn);
        fillTabIdsAssociatedWithPropertyCollection(
            associatedPropertyCollectionVerticesWithReadPermission, tabIds);
        fillAssociatedSectionIds(associatedPropertyCollectionVertices, new HashSet<>(),
            new HashSet<>(), helperModel);
      }
      Set<Vertex> associatedContextVertices = typeIdVsAssociatedContextVertices.get(taxonomyId);
      Set<Vertex> associatedContextVerticesWithReadPermission = getAssociatedContextVerticesWithReadPermission(
          associatedContextVertices, helperModel);
      fillTabIdsAssociatedWithContexts(associatedContextVerticesWithReadPermission, tabIds);
      Set<Vertex> associatedRelationshipVertices = typeIdVsAssociatedRelationshipVertices
          .get(taxonomyId);
      Set<Vertex> associatedRelationshipVerticesWithReadPermission = getAssociatedRelationshipVerticesWithReadPermission(
          associatedRelationshipVertices, mapToReturn, helperModel.getNatureRelationshipIds());
      fillTabIdsAssociatedWithRelationships(associatedRelationshipVerticesWithReadPermission,
          tabIds);
      fillAssociatedSectionIds(new HashSet<>(), associatedContextVertices,
          associatedRelationshipVertices, helperModel);
    }
  }
  
  protected void fillTabIdsAssociatedWithNatureKlass(IGetConfigDetailsHelperModel helperModel,
      Set<String> tabIds, Map<String, Object> mapToReturn)
  {
    Map<String, Set<Vertex>> typeIdVsAssociatedContextVertices = helperModel
        .getTypeIdVsAssociatedContextVertices();
    Map<String, Set<Vertex>> typeIdVsAssociatedRelationshipVertices = helperModel
        .getTypeIdVsAssociatedRelationshipsVertices();
    Map<String, Set<Vertex>> typeIdVsAssociatedPropertyCollectionVertices = helperModel
        .getTypeIdVsAssociatedPropertyCollectionVertices();
    Vertex natureKlassVertex = helperModel.getNatureNode();
    String natureKlassId = UtilClass.getCodeNew(natureKlassVertex);
    Set<Vertex> associatedPropertyCollectionVertices = typeIdVsAssociatedPropertyCollectionVertices
        .get(natureKlassId);
    Set<Vertex> associatedPropertyCollectionVerticesWithReadPermission = getAssociatedPropertyCollectionVerticesWithReadPermission(
        associatedPropertyCollectionVertices, mapToReturn);
    fillTabIdsAssociatedWithPropertyCollection(
        associatedPropertyCollectionVerticesWithReadPermission, tabIds);
    Set<Vertex> associatedContextVertices = typeIdVsAssociatedContextVertices.get(natureKlassId);
    Set<Vertex> associatedContextVerticesWithReadPermission = getAssociatedContextVerticesWithReadPermission(
        associatedContextVertices, helperModel);
    fillTabIdsAssociatedWithContexts(associatedContextVerticesWithReadPermission, tabIds);
    Set<Vertex> associatedRelationshipVertices = typeIdVsAssociatedRelationshipVertices
        .get(natureKlassId);
    Set<Vertex> associatedRelationshipVerticesWithReadPermission = getAssociatedRelationshipVerticesWithReadPermission(
        associatedRelationshipVertices, mapToReturn, helperModel.getNatureRelationshipIds());
    fillTabIdsAssociatedWithRelationships(associatedRelationshipVerticesWithReadPermission, tabIds);
    fillAssociatedSectionIds(associatedPropertyCollectionVertices, associatedContextVertices,
        associatedRelationshipVertices,  helperModel);
  }
  
  private Set<Vertex> getAssociatedRelationshipVerticesWithReadPermission(
      Set<Vertex> associatedRelationshipVertices, Map<String, Object> mapToReturn,
      Set<String> natureRelationshipIds)
  {
    Set<Vertex> associatedRelationshipVerticesWithReadPermission = new HashSet<>();
    if (associatedRelationshipVertices == null) {
      return associatedRelationshipVerticesWithReadPermission;
    }
    Map<String, Object> referencedPermissions = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_PERMISSIONS);
    Set<String> visibleRelationshipIds = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.VISIBLE_RELATIONSHIP_IDS);
    for (Vertex associatedRelationshipVertex : associatedRelationshipVertices) {
      String natureRelationshipId = UtilClass.getCodeNew(associatedRelationshipVertex);
      if (visibleRelationshipIds.contains(natureRelationshipId)
          && !natureRelationshipIds.contains(natureRelationshipId)) {
        associatedRelationshipVerticesWithReadPermission.add(associatedRelationshipVertex);
      }
    }
    return associatedRelationshipVerticesWithReadPermission;
  }
  
  private Set<Vertex> getAssociatedContextVerticesWithReadPermission(
      Set<Vertex> associatedContextVertices, IGetConfigDetailsHelperModel helperModel)
  {
    Set<Vertex> associatedContextVertexWithReadPermission = new HashSet<>();
    Map<String, String> klassIdVsContextId = helperModel.getKlassIdVsContextId();
    Collection<String> contextIdsWithRP = klassIdVsContextId.values();
    associatedContextVertices.forEach(associatedContextVertex -> {
      if (contextIdsWithRP.contains(UtilClass.getCodeNew(associatedContextVertex))) {
        associatedContextVertexWithReadPermission.add(associatedContextVertex);
      }
    });
    return associatedContextVertexWithReadPermission;
  }
  
  private Set<Vertex> getAssociatedPropertyCollectionVerticesWithReadPermission(
      Set<Vertex> associatedPropertyCollectionVertices, Map<String, Object> mapToReturn)
  {
    Map<String, Object> referencedPermissions = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_PERMISSIONS);
    Set<String> visiblePropertyCollectionIds = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.VISIBLE_PROPERTY_COLLECTION_IDS);
    Set<String> propertyCollectionIdsWithPermission = new HashSet<>(visiblePropertyCollectionIds);
    Set<Vertex> associatedPropertyCollectionVerticesWithPermission = new HashSet<>();
    for (Vertex associatedPropertyCollectionVertex : associatedPropertyCollectionVertices) {
      if (propertyCollectionIdsWithPermission
          .contains(UtilClass.getCodeNew(associatedPropertyCollectionVertex))) {
        associatedPropertyCollectionVerticesWithPermission.add(associatedPropertyCollectionVertex);
      }
    }
    return associatedPropertyCollectionVerticesWithPermission;
  }
  
  private void fillAssociatedSectionIds(Set<Vertex> associatedPropertyCollectionVertices,
      Set<Vertex> associatedContextVertices, Set<Vertex> associatedRelationshipVertices,
      IGetConfigDetailsHelperModel helperModel)
  {
    Set<String> associatedSectionIds = helperModel.getAssociatedSectionIds();
    if (associatedSectionIds == null) {
      return;
    }
    if (associatedPropertyCollectionVertices != null) {
      associatedSectionIds
          .addAll(UtilClass.getCodes(new ArrayList<>(associatedPropertyCollectionVertices)));
    }
    if (associatedContextVertices != null) {
      associatedSectionIds.addAll(UtilClass.getCodes(new ArrayList<>(associatedContextVertices)));
    }
    if (associatedRelationshipVertices != null) {
      associatedSectionIds
          .addAll(UtilClass.getCodes(new ArrayList<>(associatedRelationshipVertices)));
    }
  }
  
  protected void fillTabIdsAssociatedWithRelationships(Set<Vertex> associatedRelationshipVertices,
      Set<String> tabIds)
  {
    // if relationship is not associated with taxonomy or collection or klass
    if (associatedRelationshipVertices == null) {
      return;
    }
    for (Vertex relationshipVertex : associatedRelationshipVertices) {
      if (SystemLevelIds.STANDARD_ARTICLE_GOLDEN_ARTICLE_RELATIONSHIP_ID
          .equals(UtilClass.getCodeNew(relationshipVertex))) {
        continue;
      }
      Iterable<Vertex> tabVertices = relationshipVertex.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_TAB);
      for (Vertex tabVertex : tabVertices) {
        tabIds.add(UtilClass.getCodeNew(tabVertex));
      }
    }
  }
  
  protected void fillTabIdsAssociatedWithReferences(Set<Vertex> associatedReferencesVertices,
      Set<String> tabIds)
  {
    if (associatedReferencesVertices == null) {
      return;
    }
    for (Vertex referenceVertex : associatedReferencesVertices) {
      Iterable<Vertex> tabVertices = referenceVertex.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_TAB);
      for (Vertex tabVertex : tabVertices) {
        tabIds.add(UtilClass.getCodeNew(tabVertex));
      }
    }
  }
  
  protected void fillTabIdsAssociatedWithContexts(Set<Vertex> associatedContextVertices,
      Set<String> tabIds)
  {
    for (Vertex contextVertex : associatedContextVertices) {
      Iterable<Vertex> tabVertices = contextVertex.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_TAB);
      for (Vertex tabVertex : tabVertices) {
        tabIds.add(UtilClass.getCodeNew(tabVertex));
      }
    }
  }
  
  protected void fillTabIdsAssociatedWithPropertyCollection(
      Set<Vertex> associatedPropertyCollectionVertices, Set<String> tabIds,
      Set<String> propertyCollectionIdsToExclude)
  {
    for (Vertex propertyCollectionVertex : associatedPropertyCollectionVertices) {
      if (propertyCollectionIdsToExclude != null && !propertyCollectionIdsToExclude.isEmpty()
          && propertyCollectionIdsToExclude
              .contains(UtilClass.getCodeNew(propertyCollectionVertex))) {
        continue;
      }
      Iterable<Vertex> tabVertices = propertyCollectionVertex.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_TAB);
      for (Vertex tabVertex : tabVertices) {
        tabIds.add(UtilClass.getCodeNew(tabVertex));
      }
    }
  }
  
  protected void fillTabIdsAssociatedWithPropertyCollection(
      Set<Vertex> associatedPropertyCollectionVertices, Set<String> tabIds)
  {
    fillTabIdsAssociatedWithPropertyCollection(associatedPropertyCollectionVertices, tabIds,
        new HashSet<>());
  }
  
  protected void fillMandatoryReferencedAttributes(Map<String, Object> mapToReturn) throws Exception
  {
    Map<String, Object> referencedAttributes = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsModel.REFERENCED_ATTRIBUTES);
    Iterable<Vertex> attributeNodes = UtilClass.getVerticesByIndexedIds(IStandardConfig.StandardProperty.MandatoryAttributeCodes,
        VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
    for (Vertex attributeNode : attributeNodes) {
      Map<String, Object> attribute = AttributeUtils.getAttributeMap(attributeNode);
      referencedAttributes.put(UtilClass.getCode(attributeNode), attribute);
    }
  }
  
  protected String getTabTypeById(String tabId)
  {
    switch (tabId) {
      case SystemLevelIds.TASK_TAB:
        return CommonConstants.TEMPLATE_TASKS_TAB_BASETYPE;
      case SystemLevelIds.TIMELINE_TAB:
        return CommonConstants.TEMPLATE_TIME_LINE_TAB_BASETYPE;
      default:
        return CommonConstants.CUSTOM_TEMPLATE_TAB_BASETYPE;
    }
  }
  
  protected List<String> getDefaultRuntimeTabs(IGetConfigDetailsHelperModel helperModel)
      throws Exception
  {
    List<String> defaultRuntimeTabs = new ArrayList<>(
        IStandardConfig.StandardTab.DefaultRuntimeTabs);
    
    return defaultRuntimeTabs;
  }
  
  /**
   * fills referencedTasks for the associated klassIds
   *
   * @author Arshad
   * @param klassIds
   * @param mapToReturn
   * @throws Exception
   */
  protected void fillReferencedTasks(Vertex klassVertex, Map<String, Object> mapToReturn, String roleId)
      throws Exception
  {
    Map<String, Object> referencedTasks = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TASKS);
    Map<String, Object> referencedTags = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAGS);
    
    Iterable<Vertex> taskVertices = klassVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_TASK);
    for (Vertex taskVertex : taskVertices) {
      String taskId = UtilClass.getCodeNew(taskVertex);
      if (referencedTasks.get(taskId) != null) {
        continue;
      }
      
      Map<String, Object> taskMap = TasksUtil.getTaskMapFromNode(taskVertex);
     if(TasksUtil.isAnyPermissionsAvailableForTaskInstanceToUser(roleId,taskId)) {
           referencedTasks.put(taskId, taskMap);
     }
      String statusTagId = (String) taskMap.get(ITask.STATUS_TAG);
      if (statusTagId != null && !referencedTags.containsKey(statusTagId)) {
        Vertex statusTag = UtilClass.getVertexById(statusTagId, VertexLabelConstants.ENTITY_TAG);
        Map<String, Object> referencedTag = TagUtils.getTagMap(statusTag, true);
        referencedTags.put(statusTagId, referencedTag);
      }
      String priorityTagId = (String) taskMap.get(ITask.PRIORITY_TAG);
      if (priorityTagId != null && !referencedTags.containsKey(priorityTagId)) {
        Vertex priorityTag = UtilClass.getVertexById(priorityTagId,
            VertexLabelConstants.ENTITY_TAG);
        Map<String, Object> referencedTag = TagUtils.getTagMap(priorityTag, true);
        referencedTags.put(priorityTagId, referencedTag);
      }
    }
  }
  
}
