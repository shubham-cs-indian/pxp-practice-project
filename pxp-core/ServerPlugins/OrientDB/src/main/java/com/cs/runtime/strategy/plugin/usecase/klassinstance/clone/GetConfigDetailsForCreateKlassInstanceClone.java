package com.cs.runtime.strategy.plugin.usecase.klassinstance.clone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.model.GetConfigDetailsHelperModel;
import com.cs.config.strategy.plugin.model.IGetConfigDetailsHelperModel;
import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.klass.IKlassRelationshipSide;
import com.cs.core.config.interactor.entity.propertycollection.ISectionAttribute;
import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;
import com.cs.core.config.interactor.entity.propertycollection.ISectionTag;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.config.interactor.model.klass.IReferencedSectionRelationshipModel;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCreateKlassInstanceCloneModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.runtime.strategy.plugin.usecase.base.AbstractGetConfigDetails;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings("unchecked")
public class GetConfigDetailsForCreateKlassInstanceClone extends AbstractGetConfigDetails {
  
  public GetConfigDetailsForCreateKlassInstanceClone(
      final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForCreateKlassInstanceClone/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    return getConfigDetails(requestMap);
  }
  
  protected Map<String, Object> getConfigDetails(Map<String, Object> requestMap) throws Exception
  {
    List<String> klassIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.KLASS_IDS);
    List<String> taxonomyIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.SELECTED_TAXONOMY_IDS);
    
    String organizationId = (String) requestMap
        .get(IMulticlassificationRequestModel.ORAGANIZATION_ID);
    String endpointId = (String) requestMap.get(IMulticlassificationRequestModel.ENDPOINT_ID);
    String physicalCatalogId = (String) requestMap
        .get(IMulticlassificationRequestModel.PHYSICAL_CATALOG_ID);
    String contextId = (String) requestMap.get(IMulticlassificationRequestModel.CONTEXT_ID);
    
    IGetConfigDetailsHelperModel helperModel = new GetConfigDetailsHelperModel();
    helperModel.setOrganizationId(organizationId);
    helperModel.setEndpointId(endpointId);
    helperModel.setPhysicalCatalogId(physicalCatalogId);
    helperModel.setShouldUseTagIdTagValueIdsMap(false);
    
    Map<String, Object> referencedDataRuleMap = new HashMap<>();
    Map<String, Object> mapToReturn = getMapToReturn(referencedDataRuleMap);
    
    consolidateTaxonomyHierarchyIds(taxonomyIds, mapToReturn);
    
    fillReferencedElementsAndReferencedAttributesAndTags(mapToReturn, klassIds,
        VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS, helperModel);
    fillReferencedElementsAndReferencedAttributesAndTags(mapToReturn, taxonomyIds,
        VertexLabelConstants.ROOT_KLASS_TAXONOMY, helperModel);
    
    fillReferencedElementInRespectiveMap(mapToReturn, helperModel);
    fillMandatoryReferencedAttributes(mapToReturn);
    
    fillDataRules(mapToReturn, klassIds, referencedDataRuleMap, helperModel);
    List<String> klassAndTaxonomyIds = new ArrayList<>(klassIds);
    klassAndTaxonomyIds.addAll(taxonomyIds);
    // fillRelationshipsProperties(klassAndTaxonomyIds, mapToReturn);
    
    //will be used in case of manual creation of linked variant
    fillIsDuplicateAllowedFromContext(mapToReturn, contextId);
    
    return mapToReturn;
  }
  
  private void fillIsDuplicateAllowedFromContext(Map<String, Object> mapToReturn, String contextId)
      throws Exception
  {
    if (contextId == null || contextId.isEmpty()) {
      return;
    }
    Vertex contextNode = UtilClass.getVertexByIndexedId(contextId,
        VertexLabelConstants.VARIANT_CONTEXT);
    
    Boolean isAllowedDuplicate = contextNode.getProperty(IVariantContext.IS_DUPLICATE_VARIANT_ALLOWED);
    
    mapToReturn.put(IGetConfigDetailsForCreateKlassInstanceCloneModel.IS_DUPLICATION_ALLOWED,
        isAllowedDuplicate);
  }
  
  private void fillDataRules(Map<String, Object> mapToReturn, List<String> klassIds,
      Map<String, Object> referencedDataRuleMap, IGetConfigDetailsHelperModel helperModel)
      throws Exception
  {
    for (String klassId : klassIds) {
      Vertex klassVertex = UtilClass.getVertexById(klassId,
          VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      fillDataRulesOfKlass(klassVertex, referencedDataRuleMap, helperModel,
          RelationshipLabelConstants.HAS_KLASS_RULE_LINK);
    }
  }
  
  protected void fillReferencedElementsAndReferencedAttributesAndTags(
      Map<String, Object> mapToReturn, List<String> typeIds, String nodeLabel,
      IGetConfigDetailsHelperModel helperModel) throws Exception
  {
    List<String> referencedRelationshipProperties = (List<String>) mapToReturn
        .get(IGetConfigDetailsForCreateKlassInstanceCloneModel.REFERENCED_RELATIONSHIPS_IDS);
    
    List<String> sideIds = (List<String>) mapToReturn
        .get(IGetConfigDetailsForCreateKlassInstanceCloneModel.SIDE_IDS);
    
    List<String> identifierAttributeIds = (List<String>) mapToReturn
        .get(IGetConfigDetailsForCreateKlassInstanceCloneModel.IDENTIFIER_ATTRIBUTE_IDS);
    
    Map<String, Object> referencedElements = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_ELEMENTS);
    List<String> mandatoryAttributeIds = (List<String>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.MANDATORY_ATTRIBUTE_IDS);
    List<String> mandatoryTagIds = (List<String>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.MANDATORY_TAG_IDS);
    List<String> shouldAttributeIds = (List<String>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.SHOULD_ATTRIBUTE_IDS);
    List<String> shouldTagIds = (List<String>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.SHOULD_TAG_IDS);
    
    for (String id : typeIds) {
      Vertex klassVertex = UtilClass.getVertexByIndexedId(id, nodeLabel);
      Iterable<Vertex> kPNodesIterable = klassVertex.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_KLASS_PROPERTY);
      for (Vertex klassPropertyNode : kPNodesIterable) {
        Iterator<Vertex> entityIterator = klassPropertyNode
            .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
            .iterator();
        Vertex entityNode = entityIterator.next();
        String entityId = UtilClass.getCodeNew(entityNode);
        String entityType = klassPropertyNode.getProperty(CommonConstants.TYPE);
        if (entityType.equals(CommonConstants.ATTRIBUTE_PROPERTY)
            || entityType.equals(CommonConstants.TAG_PROPERTY)) {
          Map<String, Object> referencedElementMap = UtilClass.getMapFromNode(klassPropertyNode);
          referencedElementMap.put(CommonConstants.ID_PROPERTY, entityId);
          if (entityType.equals(CommonConstants.TAG_PROPERTY)) {
            helperModel.getTagIds()
                .add(entityId);
            List<Map<String, Object>> defaultTagValues = KlassUtils
                .getDefaultTagValuesOfKlassPropertyNode(klassPropertyNode);
            referencedElementMap.put(ISectionTag.DEFAULT_VALUE, defaultTagValues);
            List<String> selectedTagValues = KlassUtils
                .getSelectedTagValuesListOfKlassPropertyNode(klassPropertyNode);
            referencedElementMap.put(CommonConstants.SELECTED_TAG_VALUES_LIST, selectedTagValues);
            fillMandatoryShouldPropertyIds(mandatoryTagIds, shouldTagIds, referencedElementMap);
          }
          else {
            helperModel.getAttributeIds()
                .add(entityId);
            fillMandatoryShouldPropertyIds(mandatoryAttributeIds, shouldAttributeIds,
                referencedElementMap);
            Boolean isIdentifierAttribute = (Boolean) referencedElementMap
                .get(ISectionAttribute.IS_IDENTIFIER);
            if (isIdentifierAttribute != null && isIdentifierAttribute
                && !identifierAttributeIds.contains(entityId)) {
              identifierAttributeIds.add(entityId);
            }
          }
          if (referencedElements.containsKey(entityId)) {
            Map<String, Object> existingReferencedElement = (Map<String, Object>) referencedElements
                .get(entityId);
            mergeReferencedElement(referencedElementMap, existingReferencedElement, id, entityType,
                true);
          }
          else {
            referencedElements.put(entityId, referencedElementMap);
          }
        }
        else if (entityType.equals(CommonConstants.RELATIONSHIP)) {
          String relationshipId = klassPropertyNode.getProperty(ISectionElement.PROPERTY_ID);
          Map<String, Object> relationshipSide = klassPropertyNode
              .getProperty(IReferencedSectionRelationshipModel.RELATIONSHIP_SIDE);
          String sourceCardinality = (String) relationshipSide
              .get(IKlassRelationshipSide.SOURCE_CARDINALITY);
          if (!sourceCardinality.equals(CommonConstants.CARDINALITY_ONE)) {
            sideIds.add(UtilClass.getCodeNew(klassPropertyNode));
          }
          referencedRelationshipProperties.add(relationshipId);
        }
      }
      fillReferencedTagsAndLifeCycleStatusTags(mapToReturn, klassVertex);
    }
  }
  
  /*  @Override
  protected void fillRelationshipsProperties(List<String> klassIds, Map<String, Object> mapToReturn)
      throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Map<String, Object> referencedRelationshipProperties = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCreateKlassInstanceCloneModel.REFERENCED_RELATIONSHIPS_IDS);
    List<String> sideIds = (List<String>) mapToReturn
        .get(IGetConfigDetailsForCreateKlassInstanceCloneModel.SIDE_IDS);
  
    String query = "select from (select expand(in('has_property')) from "
        + VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP
        + ") where in('has_klass_property') contains (code in " + EntityUtil.quoteIt(klassIds) + ")";
    Iterable<Vertex> iterable = graph.command(new OCommandSQL(query))
        .execute();
    for (Vertex kRNode : iterable) {
      Iterator<Vertex> relationshipIterator = kRNode
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
          .iterator();
      Vertex relationshipNode = relationshipIterator.next();
      String relationshipId = UtilClass.getCode(relationshipNode);
      Map<String, Object> relationshipSide = kRNode
          .getProperty(IReferencedSectionRelationshipModel.RELATIONSHIP_SIDE);
      String sourceCardinality = (String) relationshipSide
          .get(IKlassRelationshipSide.SOURCE_CARDINALITY);
      if (!sourceCardinality.equals(CommonConstants.CARDINALITY_ONE)) {
        sideIds.add(UtilClass.getCode(kRNode));
      }
      String label = (String) UtilClass.getValueByLanguage(relationshipNode,
          CommonConstants.LABEL_PROPERTY);
      Map<String, Object> relationshipPropertiesMap = new HashMap<>();
      relationshipPropertiesMap.put(IReferencedRelationshipPropertiesModel.LABEL, label);
      RelationshipUtils.populatePropetiesInfoNew(relationshipNode, relationshipPropertiesMap);
      referencedRelationshipProperties.put(relationshipId, relationshipPropertiesMap);
    }
  }*/
  
  @Override
  protected void fillReferencedElementInRespectiveMap(Map<String, Object> mapToReturn,
      IGetConfigDetailsHelperModel helperModel) throws Exception
  {
    Map<String, Object> referencedAttributes = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_ATTRIBUTES);
    Map<String, Object> referencedTags = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAGS);
    Map<String, Object> referencedRoles = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_ROLES);
    Map<String, Object> referencedElements = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_ELEMENTS);
    Set<String> attributeIds = helperModel.getAttributeIds();
    Set<String> roleIds = helperModel.getRoleIds();
    Set<String> tagIds = helperModel.getTagIds();
    
    for (String entityId : referencedElements.keySet()) {
      Map<String, Object> entity = new HashMap<>();
      if (attributeIds.contains(entityId)) {
        Vertex entityNode = UtilClass.getVertexById(entityId,
            VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
        entity = AttributeUtils.getAttributeMap(entityNode);
        if (entity.get(IAttribute.TYPE)
            .equals(Constants.CALCULATED_ATTRIBUTE_TYPE)
            || entity.get(IAttribute.TYPE)
                .equals(Constants.CONCATENATED_ATTRIBUTE_TYPE)) {
          AttributeUtils.fillDependentAttributesForCalculatedOrConcatenatedAttributes(
              referencedAttributes, referencedTags, entity);
        }
        if (!referencedAttributes.containsKey(entityId)) {
          referencedAttributes.put(entityId, entity);
        }
        Map<String, Object> referencedElementMap = (Map<String, Object>) referencedElements
            .get(entityId);
        String defaultValue = (String) referencedElementMap.get(ISectionAttribute.DEFAULT_VALUE);
        if (defaultValue == null || defaultValue.equals("")) {
          referencedElementMap.put(ISectionAttribute.DEFAULT_VALUE,
              entity.get(IAttribute.DEFAULT_VALUE));
        }
        String defaultValueAsHtml = (String) referencedElementMap
            .get(ISectionAttribute.VALUE_AS_HTML);
        if (defaultValueAsHtml == null || defaultValueAsHtml.equals("")) {
          referencedElementMap.put(ISectionAttribute.VALUE_AS_HTML,
              entity.get(IAttribute.VALUE_AS_HTML));
        }
      }
      
      if (tagIds.contains(entityId)) {
        // Only filter tag values for types mentioned in list below
        List<String> tagTypes = Arrays.asList(SystemLevelIds.YES_NEUTRAL_NO_TAG_TYPE_ID,
            SystemLevelIds.YES_NEUTRAL_TAG_TYPE_ID, SystemLevelIds.RANGE_TAG_TYPE_ID);
        Vertex entityNode = UtilClass.getVertexById(entityId, VertexLabelConstants.ENTITY_TAG);
        if (helperModel.getShouldUseTagIdTagValueIdsMap()
            && tagTypes.contains(entityNode.getProperty(ITag.TAG_TYPE))) {
          Map<String, List<String>> tagIdTagValueIdsMap = helperModel.getTagIdTagValueIdsMap();
          List<String> tagValueIds = tagIdTagValueIdsMap.get(entityId);
          entity = TagUtils.getTagMapWithSelectTagValues(entityNode, tagValueIds);
        }
        else {
          entity = TagUtils.getTagMap(entityNode, false);
        }
        Map<String, Object> referencedElementMap = (Map<String, Object>) referencedElements
            .get(entityId);
        List<String> selectedTagValuesList = (List<String>) referencedElementMap
            .remove(CommonConstants.SELECTED_TAG_VALUES_LIST);
        
        filterChildrenTagsInKlass(entity, selectedTagValuesList,
            (Map<String, Object>) referencedTags.get(entityId));
        
        if (!referencedTags.containsKey(entityId)) {
          referencedTags.put(entityId, entity);
        }
        String tagType = (String) referencedElementMap.get(CommonConstants.TAG_TYPE_PROPERTY);
        if (tagType != null && !tagType.equals("")) {
          entity.put(ITag.TAG_TYPE, tagType);
        }
        else {
          referencedElementMap.put(ISectionTag.TAG_TYPE, entity.get(ITag.TAG_TYPE));
        }
        
        Boolean isMultiselect = (Boolean) referencedElementMap.get(ISectionTag.IS_MULTI_SELECT);
        if (isMultiselect != null) {
          entity.put(ITag.IS_MULTI_SELECT, isMultiselect);
        }
        else {
          referencedElementMap.put(ISectionTag.IS_MULTI_SELECT, entity.get(ITag.IS_MULTI_SELECT));
        }
      }
      
      if (roleIds.contains(entityId)) {
        Vertex entityNode = UtilClass.getVertexById(entityId,
            VertexLabelConstants.ENTITY_TYPE_ROLE);
        entity = (Map<String, Object>) referencedRoles.get(entityId);
        entity = RoleUtils.getRoleEntityMap(entityNode);
        referencedRoles.put(entityId, entity);
      }
    }
  }
  
  @Override
  protected Map<String, Object> getMapToReturn(Map<String, Object> referencedDataRuleMap)
  {
    
    Map<String, Object> mapToReturn = new HashMap<>();
    Map<String, Object> referencedAttributes = new HashMap<>();
    Map<String, Object> referencedElements = new HashMap<>();
    Map<String, Object> referencedTags = new HashMap<>();
    
    mapToReturn.put(
        IGetConfigDetailsForCreateKlassInstanceCloneModel.REFERENCED_LIFECYCLE_STATUS_TAGS,
        new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForCreateKlassInstanceCloneModel.REFERENCED_RELATIONSHIPS_IDS,
        new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForCreateKlassInstanceCloneModel.REFERENCED_TAGS,
        referencedTags);
    mapToReturn.put(IGetConfigDetailsForCreateKlassInstanceCloneModel.REFERENCED_ELEMENTS,
        referencedElements);
    mapToReturn.put(IGetConfigDetailsForCreateKlassInstanceCloneModel.REFERENCED_ATTRIBUTES,
        referencedAttributes);
    mapToReturn.put(IGetConfigDetailsForCreateKlassInstanceCloneModel.REFERENCED_DATA_RULES,
        referencedDataRuleMap.values());
    mapToReturn.put(IGetConfigDetailsForCreateKlassInstanceCloneModel.SIDE_IDS, new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForCreateKlassInstanceCloneModel.MANDATORY_ATTRIBUTE_IDS,
        new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForCreateKlassInstanceCloneModel.MANDATORY_TAG_IDS,
        new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForCreateKlassInstanceCloneModel.SHOULD_ATTRIBUTE_IDS,
        new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForCreateKlassInstanceCloneModel.SHOULD_TAG_IDS,
        new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForCreateKlassInstanceCloneModel.IDENTIFIER_ATTRIBUTE_IDS,
        new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForCreateKlassInstanceCloneModel.TAXONOMY_HIERARCHIES,
        new HashMap<>());
    return mapToReturn;
  }
}
