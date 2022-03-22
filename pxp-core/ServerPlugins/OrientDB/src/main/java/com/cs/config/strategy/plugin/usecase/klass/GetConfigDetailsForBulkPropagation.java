package com.cs.config.strategy.plugin.usecase.klass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.model.GetConfigDetailsHelperModel;
import com.cs.config.strategy.plugin.model.IGetConfigDetailsHelperModel;
import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.config.strategy.plugin.usecase.base.taxonomy.util.TaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.datarule.util.GetDataRuleUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.propertycollection.ISectionAttribute;
import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;
import com.cs.core.config.interactor.entity.propertycollection.ISectionTag;
import com.cs.core.config.interactor.entity.tag.IIdRelevance;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.globalpermissions.IReferencedTemplatePermissionModel;
import com.cs.core.config.interactor.model.klass.IDefaultValueChangeModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForBulkPropagationRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForBulkPropagationResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassDetailsForBulkPropagationModel;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipPropertiesModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.runtime.strategy.plugin.usecase.base.AbstractGetConfigDetails;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings("unchecked")
public class GetConfigDetailsForBulkPropagation extends AbstractGetConfigDetails {
  
  public GetConfigDetailsForBulkPropagation(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForBulkPropagation/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String userId = (String) requestMap.get("userId");
    IGetConfigDetailsHelperModel helperModel = new GetConfigDetailsHelperModel();
    helperModel.setUserId(userId);
    Vertex userInRole = RoleUtils.getRoleFromUser(userId);

    List<String> klassIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.KLASS_IDS);
    List<String> taxonomyIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.SELECTED_TAXONOMY_IDS);
    List<String> collectionIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.COLLECTION_IDS);
    List<String> addedKlassIds = (List<String>) requestMap
        .get(IConfigDetailsForBulkPropagationRequestModel.ADDED_KLASS_IDS);
    List<String> addedTaxonomyIds = (List<String>) requestMap
        .get(IConfigDetailsForBulkPropagationRequestModel.ADDED_TAXONOMY_IDS);
    List<String> addedTypeIds = new ArrayList<>(addedKlassIds);
    addedTypeIds.addAll(addedTaxonomyIds);
    
    String organizationId = (String) requestMap
        .get(IConfigDetailsForBulkPropagationRequestModel.ORAGANIZATION_ID);
    String physicalCatalogId = (String) requestMap
        .get(IConfigDetailsForBulkPropagationRequestModel.PHYSICAL_CATALOG_ID);
    String endpointId = (String) requestMap
        .get(IConfigDetailsForBulkPropagationRequestModel.ENDPOINT_ID);
    
    Map<String, Object> mapToReturn = getMapToReturn();
    fillConfigDetailsForBulkPropagation(klassIds, addedTypeIds, mapToReturn,
        VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS, organizationId, physicalCatalogId, endpointId,
        helperModel);
    fillConfigDetailsForBulkPropagation(taxonomyIds, addedTypeIds, mapToReturn,
        VertexLabelConstants.ROOT_KLASS_TAXONOMY, organizationId, physicalCatalogId, endpointId,
        helperModel);
    fillConfigDetailsForBulkPropagation(collectionIds, addedTypeIds, mapToReturn,
        VertexLabelConstants.COLLECTION, organizationId, physicalCatalogId, endpointId,
        helperModel);
    fillReferencedTaxonomies(addedTaxonomyIds, mapToReturn);
    List<String> typeIds = new ArrayList<>();
    typeIds.addAll(klassIds);
    typeIds.addAll(taxonomyIds);
    mergeCouplingTypeFromOfReferencedElementsFromRelationship(mapToReturn, typeIds);
    List<String> parentTaxonomyIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.PARENT_TAXONOMY_IDS);
    List<String> parentKlassIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.PARENT_KLASS_IDS);
    /*fillContextualDataTransferProperties(mapToReturn, new ArrayList<>(), new ArrayList<>(),
    helperModel, isValueInheritaceFromParent, isValueInheritaceToChildren);*/
    mergeCouplingTypeOfReferencedElementsFromContext(mapToReturn, helperModel.getNatureNode(),
        parentKlassIds, parentTaxonomyIds);
    helperModel.setInstanceKlassIds(new HashSet<>(klassIds));
    fillReferencedPermission(mapToReturn, userInRole, helperModel);
    UtilClass.isLanguageInheritanceHierarchyPresent(mapToReturn,
        (List<String>) requestMap.get(IMulticlassificationRequestModel.LANGUAGE_CODES));
    return mapToReturn;
  }

  private Map<String, Object> getMapToReturn()
  {
    Map<String, Object> mapToReturn = new HashMap<>();
    mapToReturn.put(IConfigDetailsForBulkPropagationResponseModel.REFERENCED_NATURE_RELATIONSHIPS,
        new HashMap<>());
    mapToReturn.put(
        IConfigDetailsForBulkPropagationResponseModel.REFERENCED_RELATIONSHIPS_PROPERTIES,
        new HashMap<>());
    mapToReturn.put(IConfigDetailsForBulkPropagationResponseModel.REFERENCED_DATA_RULES,
        new HashMap<>());
    mapToReturn.put(IConfigDetailsForBulkPropagationResponseModel.REFERENCED_ATTRIBUTES,
        new HashMap<>());
    mapToReturn.put(IConfigDetailsForBulkPropagationResponseModel.REFERENCED_TAGS, new HashMap<>());
    mapToReturn.put(IConfigDetailsForBulkPropagationResponseModel.ENTITY_DETAILS, new HashMap<>());
    mapToReturn.put(IConfigDetailsForBulkPropagationResponseModel.REFERENCED_ELEMENTS,
        new HashMap<>());
    mapToReturn.put(IConfigDetailsForBulkPropagationResponseModel.TYPE_ID_IDENTIFIER_ATTRIBUTE_IDS,
        new HashMap<>());
    mapToReturn.put(IConfigDetailsForBulkPropagationResponseModel.DEFAULT_VALUES_DIFF,
        new ArrayList<>());
    mapToReturn.put(IConfigDetailsForBulkPropagationResponseModel.NUMBER_OF_VERSIONS_TO_MAINTAIN,
        0);
    mapToReturn.put(IConfigDetailsForBulkPropagationResponseModel.VERSIONABLE_ATTRIBUTES,
        new ArrayList<>());
    mapToReturn.put(IConfigDetailsForBulkPropagationResponseModel.VERSIONABLE_TAGS,
        new ArrayList<>());
    mapToReturn.put(IConfigDetailsForBulkPropagationResponseModel.MANDATORY_ATTRIBUTE_IDS,
        new ArrayList<>());
    mapToReturn.put(IConfigDetailsForBulkPropagationResponseModel.MANDATORY_TAG_IDS,
        new ArrayList<>());
    mapToReturn.put(IConfigDetailsForBulkPropagationResponseModel.SHOULD_ATTRIBUTE_IDS,
        new ArrayList<>());
    mapToReturn.put(IConfigDetailsForBulkPropagationResponseModel.SHOULD_TAG_IDS,
        new ArrayList<>());
    Map<String, Object> referencedPermissions = new HashMap<String, Object>();
    referencedPermissions.put(IReferencedTemplatePermissionModel.EDITABLE_PROPERTY_COLLECTION_IDS,
        new HashSet<String>());
    referencedPermissions.put(IReferencedTemplatePermissionModel.VISIBLE_PROPERTY_COLLECTION_IDS,
        new HashSet<String>());
    referencedPermissions.put(IReferencedTemplatePermissionModel.EDITABLE_PROPERTY_IDS,
        new HashSet<String>());
    referencedPermissions.put(IReferencedTemplatePermissionModel.VISIBLE_PROPERTY_IDS,
        new HashSet<String>());

    mapToReturn.put(IConfigDetailsForBulkPropagationResponseModel.REFERENCED_PERMISSIONS, referencedPermissions);
    Map<String, Object> referencedPropertyCollectionMap = new HashMap<>();
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_PROPERTY_COLLECTIONS,
        referencedPropertyCollectionMap);
    return mapToReturn;
  }
  
  /**
   * Description : For all klassIds passed, get all relationship nodes
   * connected, fetch respective side KR nodes and fill
   * referencedRelationshipProperties map\ with attribute/tags info, klassIds
   * that are connected to kR Nodes and targetTypes.
   *
   * @author Ajit
   * @param klassIds
   * @param mapToReturn
   * @param helperModel
   * @throws Exception
   * @coauthor Osho funtion also fills referencedDataRules and
   *           referencedAttributes required for data propagation
   */
  protected void fillConfigDetailsForBulkPropagation(List<String> klassIds,
      List<String> addedTypeIds, Map<String, Object> mapToReturn, String vertexLabel,
      String organizationId, String physicalCatalogId, String endpointId,
      IGetConfigDetailsHelperModel helperModel) throws Exception
  {
    Map<String, Object> klassDetails = (Map<String, Object>) mapToReturn
        .get(IConfigDetailsForBulkPropagationResponseModel.ENTITY_DETAILS);
    String hasEntityRuleLink = null;
    if (vertexLabel.equals(VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS)) {
      hasEntityRuleLink = RelationshipLabelConstants.HAS_KLASS_RULE_LINK;
    }
    else if (vertexLabel.equals(VertexLabelConstants.ROOT_KLASS_TAXONOMY)) {
      hasEntityRuleLink = RelationshipLabelConstants.HAS_TAXONOMY_RULE_LINK;
    }
    for (String klassId : klassIds) {
      Boolean isAddedKlassOrTaxonomy = false;
      if (addedTypeIds.contains(klassId)) {
        isAddedKlassOrTaxonomy = true;
      }
      Map<String, Object> klassDetail = new HashMap<>();
      klassDetails.put(klassId, klassDetail);
      klassDetail.put(IKlassDetailsForBulkPropagationModel.KLASS_ID, klassId);
      List<String> relationshipIds = fillReferencedRelationshipMapAndReturnAssociatedRelationshipIds(
          klassId, mapToReturn);
      
      klassDetail.put(IKlassDetailsForBulkPropagationModel.RELATIONSHIP_IDS, relationshipIds);
      try {
        // TODO Fix Me
        try {
          Vertex attributionVertex = UtilClass.getVertexById(klassId,
              VertexLabelConstants.ENTITY_TAG);
          if (!attributionVertex.getProperty(CommonConstants.ORIENTDB_CLASS_PROPERTY)
              .equals(VertexLabelConstants.ATTRIBUTION_TAXONOMY)) {
            continue;
          }
        }
        catch (NotFoundException e) {
          // Do Nothing
        }
        Vertex klassNode = UtilClass.getVertexById(klassId, vertexLabel);
        fillReferencedElementsAndProperties(klassNode, mapToReturn, klassDetail,
            isAddedKlassOrTaxonomy, vertexLabel, helperModel);
        fillReferencedPropertyCollections(helperModel, klassNode, mapToReturn, null);
        if (vertexLabel.equals(VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS)) {
          Integer numberOfVersionsToMaintain = (Integer) klassNode
              .getProperty(IKlass.NUMBER_OF_VERSIONS_TO_MAINTAIN);
          Integer existingNumberOfVersionsToMaintain = (Integer) mapToReturn
              .get(IConfigDetailsForBulkPropagationResponseModel.NUMBER_OF_VERSIONS_TO_MAINTAIN);
          if (numberOfVersionsToMaintain > existingNumberOfVersionsToMaintain) {
            mapToReturn.put(
                IConfigDetailsForBulkPropagationResponseModel.NUMBER_OF_VERSIONS_TO_MAINTAIN,
                numberOfVersionsToMaintain);
          }
        }
        
        List<String> dataRuleIds = fillReferencedDataRulesAndReturnAssociatedDataRuleIds(klassNode,
            mapToReturn, hasEntityRuleLink, organizationId, physicalCatalogId, endpointId);
        klassDetail.put(IKlassDetailsForBulkPropagationModel.DATA_RULE_IDS, dataRuleIds);
        Boolean isNature = klassNode.getProperty(IKlass.IS_NATURE);
        if (isNature != null && isNature) {
          List<String> referencedNatureRelationshipIds = fillReferencedNatureRelationshipMapAndReturnAssociatedNatureRelationshipIds(
              klassNode, mapToReturn);
          klassDetail.put(IKlassDetailsForBulkPropagationModel.REFERENCED_NATURE_RELATIONSHIP_IDS,
              referencedNatureRelationshipIds);
          helperModel.setNatureNode(klassNode);
        }
        else {
          helperModel.getNonNatureKlassNodes()
              .add(klassNode);
        }
        /* List<String> tempKlassIds = new ArrayList<String>();
        tempKlassIds.add(klassId);
        mergeCouplingTypeFromOfReferencedElementsFromRelationship(klassDetail, tempKlassIds);*/
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException(e);
      }
    }
  }
  
  /**
   * @param klassNode
   * @param mapToReturn
   * @return List of nature relationship ids
   * @author Kshitij
   */
  private List<String> fillReferencedNatureRelationshipMapAndReturnAssociatedNatureRelationshipIds(
      Vertex klassNode, Map<String, Object> mapToReturn) throws Exception
  {
    List<String> natureRelatinshipIds = new ArrayList<>();
    Map<String, Object> referencedNatureRelationshipMap = (Map<String, Object>) mapToReturn
        .get(IConfigDetailsForBulkPropagationResponseModel.REFERENCED_NATURE_RELATIONSHIPS);
    Iterable<Vertex> kNRs = klassNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF);
    for (Vertex kNR : kNRs) {
      Vertex nRVertex = kNR.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
          .iterator()
          .next();
      String relationshipId = UtilClass.getCodeNew(nRVertex);
      String label = (String) UtilClass.getValueByLanguage(nRVertex,
          CommonConstants.LABEL_PROPERTY);
      HashMap<String, Object> relationshipMap = RelationshipUtils
          .getRelationshipMapWithContext(nRVertex);
      relationshipMap.put(IGetReferencedNatureRelationshipModel.ID, relationshipId);
      relationshipMap.put(IGetReferencedNatureRelationshipModel.LABEL, label);
      List<String> contextTagIds = new ArrayList<>();
      Iterable<Vertex> contextTagVertices = nRVertex.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_RELATIONSHIP_CONTEXT_TAG);
      for (Vertex contextTagVertex : contextTagVertices) {
        String contextTagId = UtilClass.getCodeNew(contextTagVertex);
        contextTagIds.add(contextTagId);
      }
      relationshipMap.put(IGetReferencedNatureRelationshipModel.CONTEXT_TAGS, contextTagIds);
      natureRelatinshipIds.add(relationshipId);
      referencedNatureRelationshipMap.put(relationshipId, relationshipMap);
    }
    return natureRelatinshipIds;
  }
  
  /**
   * @author Osho
   * @description : fills referencedDataRule for klass
   * @param klassNode
   * @param mapToReturn
   * @return
   * @throws Exception
   */
  private List<String> fillReferencedDataRulesAndReturnAssociatedDataRuleIds(Vertex klassNode,
      Map<String, Object> mapToReturn, String hasEntityRuleLink, String organizationId,
      String physicalCatalogId, String endPointId) throws Exception
  {
    Map<String, Object> referencedDataRuleMap = (Map<String, Object>) mapToReturn
        .get(IConfigDetailsForBulkPropagationResponseModel.REFERENCED_DATA_RULES);
    List<String> dataRuleIds = new ArrayList<>();
    if (hasEntityRuleLink == null) {
      return dataRuleIds;
    }
    String query = getDataRulesQuery(klassNode, organizationId, physicalCatalogId, endPointId,
        hasEntityRuleLink);
    Iterable<Vertex> dataRuleVertices = executeQuery(query);
    for (Vertex dataRuleVertex : dataRuleVertices) {
      String dataRuleId = UtilClass.getCodeNew(dataRuleVertex);
      if (referencedDataRuleMap.get(dataRuleId) != null) {
        dataRuleIds.add(dataRuleId);
        continue;
      }
      Map<String, Object> dataRuleMap = GetDataRuleUtils.getDataRuleFromNode(dataRuleVertex, true);
      referencedDataRuleMap.put(dataRuleId, dataRuleMap);
    }
    
    return dataRuleIds;
  }
  
  /**
   * @author Osho
   * @description : this methods fills referencedAttributeMap required for data
   *              rule rule violation upon bulk propagation
   * @param klassId
   * @param mapToReturn
   * @param klassDetail
   * @return
   * @throws Exception
   * @coauthor Lokesh attribute context is not sent in referencedElements
   */
  private void fillReferencedElementsAndProperties(Vertex klassNode,
      Map<String, Object> mapToReturn, Map<String, Object> klassDetail,
      Boolean isAddedKlassOrTaxonomy, String vertexLabel, IGetConfigDetailsHelperModel helperModel) throws Exception
  {
    Map<String, Object> referencedAttributes = (Map<String, Object>) mapToReturn
        .get(IConfigDetailsForBulkPropagationResponseModel.REFERENCED_ATTRIBUTES);
    Map<String, Object> referencedTags = (Map<String, Object>) mapToReturn
        .get(IConfigDetailsForBulkPropagationResponseModel.REFERENCED_TAGS);
    Map<String, List<String>> typeIdIdentifierAttributeIds = (Map<String, List<String>>) mapToReturn
        .get(IConfigDetailsForBulkPropagationResponseModel.TYPE_ID_IDENTIFIER_ATTRIBUTE_IDS);
    List<String> identifierAttributeIds = new ArrayList<>();
    typeIdIdentifierAttributeIds.put(UtilClass.getCodeNew(klassNode), identifierAttributeIds);
    List<String> attributeIds = new ArrayList<>();
    klassDetail.put(IKlassDetailsForBulkPropagationModel.ATTRIBUTE_IDS, attributeIds);
    Map<String, Object> referencedElements = new HashMap<String, Object>();
    klassDetail.put(IKlassDetailsForBulkPropagationModel.REFERENCED_ELEMENTS, referencedElements);
    List<Map<String, Object>> defaultValueChangeList = new ArrayList<>();
    klassDetail.put(IKlassDetailsForBulkPropagationModel.DEFAULT_VALUES_DIFF,
        defaultValueChangeList);
    Map<String, Object> rootLevelReferencedElements = (Map<String, Object>) mapToReturn
        .get(IConfigDetailsForBulkPropagationResponseModel.REFERENCED_ELEMENTS);
    List<String> mandatoryAttributeIds = (List<String>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.MANDATORY_ATTRIBUTE_IDS);
    List<String> mandatoryTagIds = (List<String>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.MANDATORY_TAG_IDS);
    List<String> shouldAttributeIds = (List<String>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.SHOULD_ATTRIBUTE_IDS);
    List<String> shouldTagIds = (List<String>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.SHOULD_TAG_IDS);
    
    List<String> versionableAttributes = (List<String>) mapToReturn
        .get(IConfigDetailsForBulkPropagationResponseModel.VERSIONABLE_ATTRIBUTES);
    List<String> versionableTags = (List<String>) mapToReturn
        .get(IConfigDetailsForBulkPropagationResponseModel.VERSIONABLE_TAGS);
    
    Iterable<Vertex> klassPropertyNodes = klassNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_KLASS_PROPERTY);
    Map<String, Set<String>> typeIdVsAssociatedPropertyIds = helperModel
        .getTypeIdVsAssociatedPropertyIds();
    Set<String> propertyIds = new HashSet<>();
    String klassId = UtilClass.getCodeNew(klassNode);

    typeIdVsAssociatedPropertyIds.put(klassId, propertyIds);
    for (Vertex klassPropertyNode : klassPropertyNodes) {
      String propertyId = klassPropertyNode.getProperty(ISectionElement.PROPERTY_ID);
      propertyIds.add(propertyId);
      String type = klassPropertyNode.getProperty(ISectionElement.TYPE);
      if (!type.equals(CommonConstants.ATTRIBUTE) && !type.equals(CommonConstants.TAG)) {
        continue;
      }
      
      Map<String, Object> referencedElementMap = UtilClass.getMapFromNode(klassPropertyNode);
      Iterator<Vertex> klassEntityNodes = klassPropertyNode
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
          .iterator();
      Vertex entityNode = klassEntityNodes.next();
      String entityId = UtilClass.getCodeNew(entityNode);
      referencedElementMap.put(CommonConstants.ID_PROPERTY, entityId);
      referencedElements.put(entityId, referencedElementMap);
      
      if (rootLevelReferencedElements.containsKey(entityId)) {
        Map<String, Object> existingReferencedElement = (Map<String, Object>) rootLevelReferencedElements
            .get(entityId);
        mergeReferencedElement(referencedElementMap, existingReferencedElement,
            UtilClass.getCodeNew(klassPropertyNode), type, false);
      }
      else {
        rootLevelReferencedElements.put(entityId, referencedElementMap);
      }
      
      if (isAddedKlassOrTaxonomy) {
        Map<String, Object> addedPropertyMap = new HashMap<>();
        KlassUtils.fillAddedPropertyMap(addedPropertyMap, klassPropertyNode, vertexLabel);
        if (!addedPropertyMap.isEmpty()) {
          List<String> klassAndChildrenIds = new ArrayList<>();
          klassAndChildrenIds
              .add((String) klassDetail.get(IKlassDetailsForBulkPropagationModel.KLASS_ID));
          addedPropertyMap.put(IDefaultValueChangeModel.KLASS_AND_CHILDRENIDS, klassAndChildrenIds);
          defaultValueChangeList.add(addedPropertyMap);
        }
      }
      if (type.equals(CommonConstants.ATTRIBUTE)) {
        attributeIds.add(entityId);
        String attributeContextId = getAttributeContextId(klassPropertyNode);
        referencedElementMap.put(ISectionAttribute.ATTRIBUTE_VARIANT_CONTEXT, attributeContextId);
        Boolean isIdentifier = klassPropertyNode.getProperty(ISectionAttribute.IS_IDENTIFIER);
        if (isIdentifier != null && isIdentifier) {
          identifierAttributeIds.add(entityId);
        }
        Vertex entityVertex = UtilClass.getVertexById(entityId,
            VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
        Map<String, Object> entity = UtilClass.getMapFromVertex(new ArrayList<>(), entityVertex);
        Boolean isVersionable = (Boolean) referencedElementMap.get(ISectionTag.IS_VERSIONABLE);
        if (isVersionable == null) {
          referencedElementMap.put(ISectionTag.IS_VERSIONABLE, entity.get(ITag.IS_VERSIONABLE));
        }
        
        if (!versionableAttributes.contains(entityId)
            && (Boolean) referencedElementMap.get(ISectionAttribute.IS_VERSIONABLE)) {
          versionableAttributes.add(entityId);
        }
        if (referencedAttributes.get(entityId) != null) {
          continue;
        }
        Map<String, Object> attributeMap = AttributeUtils.getAttributeMap(entityNode);
        if (entityNode.getProperty(IAttribute.TYPE)
            .equals(Constants.CALCULATED_ATTRIBUTE_TYPE)
            || entityNode.getProperty(IAttribute.TYPE)
                .equals(Constants.CONCATENATED_ATTRIBUTE_TYPE)) {
          
          AttributeUtils.fillDependentAttributesForCalculatedOrConcatenatedAttributes(
              referencedAttributes, referencedTags, attributeMap);
        }
        referencedAttributes.put(entityId, attributeMap);
        fillMandatoryShouldPropertyIds(mandatoryAttributeIds, shouldAttributeIds,
            referencedElementMap);
      }
      else if (type.equals(CommonConstants.TAG)) {
        Map<String, Object> tagMap = TagUtils.getTagMap(entityNode, true);
        referencedTags.put(entityId, tagMap);
        List<Map<String, Object>> defaultTagValues = KlassUtils
            .getDefaultTagValuesOfKlassPropertyNode(klassPropertyNode);
        referencedElementMap.put(ISectionTag.DEFAULT_VALUE, defaultTagValues);
        
        Vertex entityVertex = UtilClass.getVertexById(entityId, VertexLabelConstants.ENTITY_TAG);
        Map<String, Object> entity = UtilClass.getMapFromVertex(new ArrayList<>(), entityVertex);
        Boolean isVersionable = (Boolean) referencedElementMap.get(ISectionTag.IS_VERSIONABLE);
        if (isVersionable == null) {
          referencedElementMap.put(ISectionTag.IS_VERSIONABLE, entity.get(ITag.IS_VERSIONABLE));
        }
        
        if (!versionableTags.contains(entityId)
            && (Boolean) referencedElementMap.get(ISectionAttribute.IS_VERSIONABLE)) {
          versionableTags.add(entityId);
        }
        fillMandatoryShouldPropertyIds(mandatoryTagIds, shouldTagIds, referencedElementMap);
      }
    }
    if (isAddedKlassOrTaxonomy) {
      String klassProperty = klassNode.getProperty(CommonConstants.ORIENTDB_CLASS_PROPERTY);
      // TODO : Need to handle for language taxonomy also
      if (klassProperty.equals(VertexLabelConstants.ATTRIBUTION_TAXONOMY)) {
        fillDiffListForAttributionTaxonomy(klassNode, defaultValueChangeList, referencedElements);
      }
    }
    
    ((List<Map<String, Object>>) mapToReturn
        .get(IConfigDetailsForBulkPropagationResponseModel.DEFAULT_VALUES_DIFF))
            .addAll(defaultValueChangeList);
    Vertex nameAttribute = UtilClass.getVertexById(CommonConstants.NAME_ATTRIBUTE,
        VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
    referencedAttributes.put(CommonConstants.NAME_ATTRIBUTE,
        AttributeUtils.getAttributeMap(nameAttribute));
    attributeIds.add(CommonConstants.NAME_ATTRIBUTE);
  }
  
  private String getAttributeContextId(Vertex klassPropertyNode)
  {
    String klassPropertyContextId = null;
    Iterator<Vertex> attributeContextsIterator = klassPropertyNode
        .getVertices(Direction.IN, RelationshipLabelConstants.VARIANT_CONTEXT_OF)
        .iterator();
    
    if (attributeContextsIterator.hasNext()) {
      Vertex attributeContext = attributeContextsIterator.next();
      klassPropertyContextId = UtilClass.getCodeNew(attributeContext);
    }
    return klassPropertyContextId;
  }
  
  /**
   * @author Osho
   * @description : the method fill referencedRelationship propertyMap required
   *              for propagation
   * @param klassId
   * @param mapToReturn
   * @return
   * @throws KlassNotFoundException
   * @throws Exception
   */
  private List<String> fillReferencedRelationshipMapAndReturnAssociatedRelationshipIds(
      String klassId, Map<String, Object> mapToReturn) throws KlassNotFoundException, Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Map<String, Object> referencedRelationshipProperties = (Map<String, Object>) mapToReturn
        .get(IConfigDetailsForBulkPropagationResponseModel.REFERENCED_RELATIONSHIPS_PROPERTIES);
    
    String query = "select from (select expand(in('has_property')) from "
        + VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP
        + ") where in('has_klass_property') contains (code='" + klassId + "')";
    List<String> relationshipIds = new ArrayList<>();
    Iterable<Vertex> iterable = graph.command(new OCommandSQL(query))
        .execute();
    
    List<Vertex> klassRelationshipNodes = new ArrayList<>();
    for (Vertex kRNode : iterable) {
      klassRelationshipNodes.add(kRNode);
    }
    
    query = "select from (select expand(in('has_property')) from "
        + VertexLabelConstants.NATURE_RELATIONSHIP
        + ") where in('has_klass_property') contains (code='" + klassId + "')";
    iterable = graph.command(new OCommandSQL(query))
        .execute();
    for (Vertex kRNode : iterable) {
      klassRelationshipNodes.add(kRNode);
    }
    
    for (Vertex kRNode : klassRelationshipNodes) {
      Iterator<Vertex> relationshipIterator = kRNode
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
          .iterator();
      Vertex relationshipNode = relationshipIterator.next();
      String relationshipId = UtilClass.getCodeNew(relationshipNode);
      relationshipIds.add(relationshipId);
      String label = (String) UtilClass.getValueByLanguage(relationshipNode,
          CommonConstants.LABEL_PROPERTY);
      Map<String, Object> relationshipPropertiesMap = new HashMap<>();
      relationshipPropertiesMap.put(IReferencedRelationshipPropertiesModel.LABEL, label);
      
      RelationshipUtils.populatePropetiesInfoNew(relationshipNode, relationshipPropertiesMap);
      referencedRelationshipProperties.put(relationshipId, relationshipPropertiesMap);
    }
    return relationshipIds;
  }
  
  private void fillReferencedTaxonomies(List<String> addedTaxonomyIds,
      Map<String, Object> mapToReturn) throws Exception
  {
    Map<String, Object> referencedTaxonomyForDataRules = new HashMap<>();
    for (String taxonomyId : addedTaxonomyIds) {
      List<String> parantTaxonomyIds = new ArrayList<>();
      TaxonomyUtil.getRootParentVertexAndFillAllParentTaxonomyIds(taxonomyId, parantTaxonomyIds);
      referencedTaxonomyForDataRules.put(taxonomyId, parantTaxonomyIds);
    }
    mapToReturn.put(IConfigDetailsForBulkPropagationResponseModel.REFERENCED_TAXONOMIES,
        referencedTaxonomyForDataRules);
  }
  
  /**
   * @author Arshad This method fills default value diff list and
   *         referencedElement for attribution taxonomy(tag)
   * @param attributionTaxonomy
   * @param diffList
   * @param referencedElements
   */
  private void fillDiffListForAttributionTaxonomy(Vertex attributionTaxonomy,
      List<Map<String, Object>> diffList, Map<String, Object> referencedElements)
  {
    Iterable<Vertex> parentVertices = attributionTaxonomy.getVertices(Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    for (Vertex parentVertex : parentVertices) {
      String className = parentVertex.getProperty(CommonConstants.ORIENTDB_CLASS_PROPERTY);
      if (className.equals(VertexLabelConstants.ENTITY_TAG)) {
        Map<String, Object> tagMap = UtilClass.getMapFromNode(parentVertex);
        List<Map<String, Object>> defaultValueListOfTag = new ArrayList<>();
        Map<String, Object> propertyMap = new HashMap<>();
        propertyMap.put(IDefaultValueChangeModel.ENTITY_ID, tagMap.get(ITag.ID));
        propertyMap.put(IDefaultValueChangeModel.TYPE, CommonConstants.TAG);
        propertyMap.put(IDefaultValueChangeModel.COUPLING_TYPE, CommonConstants.LOOSELY_COUPLED);
        propertyMap.put(IDefaultValueChangeModel.IS_MANDATORY, false);
        propertyMap.put(IDefaultValueChangeModel.IS_SHOULD, false);
        propertyMap.put(IDefaultValueChangeModel.IS_SKIPPED, false);
        propertyMap.put(IDefaultValueChangeModel.SOURCE_TYPE, "");
        
        Map<String, Object> defaultValueMap = new HashMap<>();
        defaultValueMap.put(IIdRelevance.RELEVANCE, 100);
        defaultValueMap.put(IIdRelevance.TAGID, UtilClass.getCodeNew(attributionTaxonomy));
        defaultValueListOfTag.add(defaultValueMap);
        
        propertyMap.put(IDefaultValueChangeModel.VALUE, defaultValueListOfTag);
        diffList.add(propertyMap);
        
        Map<String, Object> referencedElement = new HashMap<>();
        referencedElement.put(ISectionTag.IS_MANDATORY, false);
        referencedElement.put(ISectionTag.IS_SHOULD, false);
        referencedElement.put(ISectionTag.IS_SKIPPED, false);
        referencedElement.put(ISectionTag.COUPLING_TYPE, CommonConstants.LOOSELY_COUPLED);
        referencedElement.put(ISectionTag.NUMBER_OF_VERSIONS_ALLOWED, 0);
        referencedElement.put(ISectionTag.TYPE, CommonConstants.TAG);
        
        referencedElements.put((String) tagMap.get(ITag.ID), referencedElement);
      }
    }
  }
  
  protected void fillReferencedPermission(Map<String, Object> responseMap, 
      Vertex userInRole, IGetConfigDetailsHelperModel helperModel) throws Exception
  {
    Map<String, Object> referencedPermissions = (Map<String, Object>) responseMap
        .get(IGetConfigDetailsModel.REFERENCED_PERMISSIONS);
    String roleId = UtilClass.getCodeNew(userInRole);
    Set<Vertex> typesAndTaxonomiesVertices = new HashSet<>();
    
    Vertex natureKlassVertex = helperModel.getNatureNode();
    typesAndTaxonomiesVertices.add(natureKlassVertex);
    
    Set<Vertex> nonNatureKlassVertices = helperModel.getNonNatureKlassNodes();
    typesAndTaxonomiesVertices.addAll(nonNatureKlassVertices);
    
    Set<Vertex> taxonomyVertices = helperModel.getTaxonomyVertices();
    typesAndTaxonomiesVertices.addAll(taxonomyVertices);
    
    for (Vertex vertex : typesAndTaxonomiesVertices) {
      fillPropertyCollectionPermissionAndEntitiesPermission(responseMap, helperModel,
          referencedPermissions, vertex, roleId);
    }
    
  }
  
  protected void fillPropertyCollectionPermissionAndEntitiesPermission(
      Map<String, Object> responseMap, IGetConfigDetailsHelperModel helperModel,
      Map<String, Object> referencedPermissions, Vertex typeVertex, String roleId) throws Exception
  {
    String typeId = UtilClass.getCodeNew(typeVertex);
    Map<String, Set<Vertex>> typeIdVsAssociatedPropertyCollectionVertices = helperModel
        .getTypeIdVsAssociatedPropertyCollectionVertices();
    Set<Vertex> referencedPropertyCollection = typeIdVsAssociatedPropertyCollectionVertices
        .get(typeId);
    if (referencedPropertyCollection != null) {
      fillPropertyCollectionPermission(roleId, typeId, referencedPropertyCollection,
          referencedPermissions);
    }
    
    Set<String> propertyIds = helperModel.getTypeIdVsAssociatedPropertyIds()
        .get(typeId);
    if (propertyIds != null) {
      fillPropertyPermission(roleId, typeId, propertyIds, referencedPermissions);
    }
  }
  
}
