package com.cs.config.strategy.plugin.usecase.versionrollback;

import com.cs.config.strategy.plugin.model.GetConfigDetailsHelperModel;
import com.cs.config.strategy.plugin.model.IGetConfigDetailsHelperModel;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.propertycollection.ISectionAttribute;
import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.exception.versionrollback.RollbackFailedDueToNatureClassDeletedException;
import com.cs.core.config.interactor.model.attributiontaxonomy.IMasterTaxonomyModel;
import com.cs.core.config.interactor.model.globalpermissions.IReferencedTemplatePermissionModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.config.interactor.model.versionrollback.IPropertyCouplingInformationModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForVersionRollbackModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.runtime.strategy.plugin.usecase.klassinstance.GetConfigDetailsWithoutPermissions;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class GetConfigDetailsForVersionRollback extends GetConfigDetailsWithoutPermissions {
  
  public GetConfigDetailsForVersionRollback(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForVersionRollback/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> referencedDataRuleMap = new HashMap<>();
    Map<String, Object> mapToReturn = getMapToReturn(referencedDataRuleMap);
    String userId = (String) requestMap.get(IMulticlassificationRequestModel.USER_ID);
    Vertex userInRole = RoleUtils.getRoleFromUser(userId);
    fillEntityIdsHavingReadPermission(userInRole, mapToReturn);
    IGetConfigDetailsHelperModel helperModel = new GetConfigDetailsHelperModel();
    helperModel.setUserId((String) requestMap.get(IMulticlassificationRequestModel.USER_ID));
    helperModel.setOrganizationId(
        (String) requestMap.get(IMulticlassificationRequestModel.ORAGANIZATION_ID));
    helperModel
        .setEndpointId((String) requestMap.get(IMulticlassificationRequestModel.ENDPOINT_ID));
    helperModel.setPhysicalCatalogId(
        (String) requestMap.get(IMulticlassificationRequestModel.PHYSICAL_CATALOG_ID));
    
    List<String> klassIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.KLASS_IDS);
    List<String> taxonomyIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.SELECTED_TAXONOMY_IDS);
    List<String> parentKlassIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.PARENT_KLASS_IDS);
    List<String> parentTaxonomyIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.PARENT_TAXONOMY_IDS);
    
    validateIfNatureKlassExists(klassIds);
    
    fillKlassDetails(mapToReturn, klassIds, referencedDataRuleMap, helperModel,
        CommonConstants.ALL_PROPERTY);
    fillTaxonomyDetails(mapToReturn, taxonomyIds, referencedDataRuleMap, helperModel,
        CommonConstants.ALL_PROPERTY);
    
    /*List<String> collectionIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.COLLECTION_IDS);
    fillCollectionDetails(mapToReturn, collectionIds, helperModel, CommonConstants.ALL_PROPERTY);*/
    
    List<String> typeIds = new ArrayList<>();
    typeIds.addAll(klassIds);
    typeIds.addAll(taxonomyIds);
    
    mergeCouplingTypeFromOfReferencedElementsFromRelationship(mapToReturn, typeIds);
    mergeCouplingTypeOfReferencedElementsFromContext(mapToReturn, helperModel.getNatureNode(),
        parentKlassIds, parentTaxonomyIds);
    
    fillReferencedContextLinkedToKlass(helperModel, mapToReturn);
    fillReferencedElementInRespectiveMap(mapToReturn, helperModel);
    fillMandatoryReferencedAttributes(mapToReturn);
    removeUnnecessaryFieldsFromReturnMap(mapToReturn);
    fillReferencedLanguages(mapToReturn,
        (List<String>) requestMap.get(IMulticlassificationRequestModel.LANGUAGE_CODES));
    UtilClass.isLanguageInheritanceHierarchyPresent(mapToReturn,
        (List<String>) requestMap.get(IMulticlassificationRequestModel.LANGUAGE_CODES));
    return mapToReturn;
  }
  
  private void removeUnnecessaryFieldsFromReturnMap(Map<String, Object> mapToReturn)
  {
    mapToReturn.remove(IGetConfigDetailsForCustomTabModel.REFERENCED_PERMISSIONS);
    mapToReturn.remove(IGetConfigDetailsForCustomTabModel.PERSONAL_TASK_IDS);
    mapToReturn.remove(IGetConfigDetailsForCustomTabModel.TAXONOMY_HIERARCHIES);
    mapToReturn.remove(IGetConfigDetailsForCustomTabModel.REFERENCED_TEMPLATES);
    mapToReturn.remove(IGetConfigDetailsForCustomTabModel.REFERENCED_TASKS);
    mapToReturn.remove(IGetConfigDetailsForCustomTabModel.VARIANT_CONTEXT_IDS);
    mapToReturn.remove(IGetConfigDetailsForCustomTabModel.PROMOTION_VERSION_CONTEXT_IDS);
    mapToReturn.remove(IGetConfigDetailsForCustomTabModel.LANGAUGE_CONTEXT_IDS);
    mapToReturn.remove(IGetConfigDetailsForCustomTabModel.RELATIONSHIP_REFERENCED_ELEMENTS);
    mapToReturn.remove(IGetConfigDetailsForCustomTabModel.REFERENCED_ROLES);
    mapToReturn.remove(IGetConfigDetailsForCustomTabModel.REFERENCED_RELATIONSHIPS_MAPPING);
  }
  
  /**
   * Description: Exception is thrown if at least one nature klass is not there.
   *
   * @author Niraj
   * @param klassIds
   * @throws RollbackFailedDueToNatureClassDeletedException
   */
  private void validateIfNatureKlassExists(List<String> klassIds)
      throws RollbackFailedDueToNatureClassDeletedException
  {
    String query = "select from " + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS + " where "
        + IKlass.IS_NATURE + " = true and " + CommonConstants.CODE_PROPERTY + " in "
        + EntityUtil.quoteIt(klassIds);
    
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    Iterator<Vertex> iterator = vertices.iterator();
    
    if (!iterator.hasNext()) {
      throw new RollbackFailedDueToNatureClassDeletedException();
    }
  }
  
  protected Map<String, Object> getMapToReturn(Map<String, Object> referencedDataRuleMap)
  {
    Map<String, Object> mapToReturn = new HashMap<>();
    
    Map<String, Object> referencedNatureRelationships = new HashMap<>();
    Map<String, Object> referencedRelationshipProperties = new HashMap<>();
    Map<String, Object> referencedVariantContextsMap = new HashMap<>();
    Map<String, Object> embeddedVariantContexts = new HashMap<>();
    Map<String, Object> productVariantContexts = new HashMap<>();
    Map<String, Object> referencedPropertyCouplingInformation = new HashMap<>();
    Map<String, Object> referencedRelationshipMap = new HashMap<>();
    Map<String, String> relationshipReferencedElements = new HashMap<>();
    List<String> referencedLifeCycleStatusTags = new ArrayList<>();
    Map<String, Object> referencedTags = new HashMap<>();
    Map<String, Object> referencedAttribute = new HashMap<>();
    Map<String, Object> typeIdIdentifierAttributeIds = new HashMap<>();
    Map<String, Object> referencedPermissions = new HashMap<String, Object>();
    Map<String, Object> referencedKlassMap = new HashMap<>();
    Map<String, Object> referencedSectionElementMap = new HashMap<>();
    Map<String, Object> referencedTaxonomies = new HashMap<>();
    
    mapToReturn.put(IGetConfigDetailsForVersionRollbackModel.TYPEID_IDENTIFIER_ATTRIBUTEIDS,
        typeIdIdentifierAttributeIds);
    mapToReturn.put(IGetConfigDetailsForVersionRollbackModel.REFERENCED_ATTRIBUTES,
        referencedAttribute);
    mapToReturn.put(IGetConfigDetailsForVersionRollbackModel.REFERENCED_TAGS, referencedTags);
    referencedPermissions.put(IReferencedTemplatePermissionModel.ENTITIES_HAVING_RP,
        new HashSet<String>());
    referencedVariantContextsMap.put(IReferencedContextModel.EMBEDDED_VARIANT_CONTEXTS,
        embeddedVariantContexts);
    referencedVariantContextsMap.put(IReferencedContextModel.PRODUCT_VARIANT_CONTEXTS,
        productVariantContexts);
    referencedVariantContextsMap.put(IReferencedContextModel.RELATIONSHIP_VARIANT_CONTEXTS,
        new HashMap<>());
    
    mapToReturn.put(
        IGetConfigDetailsForVersionRollbackModel.REFERENCED_PROPERTY_COUPLING_INFORMATION,
        referencedPropertyCouplingInformation);
    mapToReturn.put(
        IGetConfigDetailsForVersionRollbackModel.TECHNICAL_IMAGE_VARIANT_CONTEXT_WITH_AUTO_CREATE_ENABLE,
        new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForVersionRollbackModel.REFERENCED_RELATIONSHIPS_PROPERTIES,
        referencedRelationshipProperties);
    mapToReturn.put(IGetConfigDetailsForVersionRollbackModel.REFERENCED_NATURE_RELATIONSHIPS,
        referencedNatureRelationships);
    mapToReturn.put(IGetConfigDetailsForVersionRollbackModel.REFERENCED_VARIANT_CONTEXTS,
        referencedVariantContextsMap);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.RELATIONSHIP_REFERENCED_ELEMENTS,
        relationshipReferencedElements);
    mapToReturn.put(IGetConfigDetailsForVersionRollbackModel.REFERENCED_RELATIONSHIPS,
        referencedRelationshipMap);
    mapToReturn.put(IGetConfigDetailsForVersionRollbackModel.REFERENCED_LIFECYCLE_STATUS_TAGS,
        referencedLifeCycleStatusTags);
    
    Map<String, Object> referencedContextProperties = new HashMap<>();
    mapToReturn.put(IGetConfigDetailsModel.REFERENCED_KLASSES, referencedKlassMap);
    mapToReturn.put(IGetConfigDetailsModel.REFERENCED_ELEMENTS, referencedSectionElementMap);
    mapToReturn.put(IGetConfigDetailsModel.NUMBER_OF_VERSIONS_TO_MAINTAIN, 0);
    mapToReturn.put(IGetConfigDetailsModel.REFERENCED_TAXONOMIES, referencedTaxonomies);
    mapToReturn.put(IGetConfigDetailsModel.REFERENCED_LIFECYCLE_STATUS_TAGS,
        referencedLifeCycleStatusTags);
    mapToReturn.put(IGetConfigDetailsModel.REFERENCED_ROLES, new HashMap<>());
    mapToReturn.put(IGetConfigDetailsModel.LANGAUGE_CONTEXT_IDS, new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsModel.VARIANT_CONTEXT_IDS, new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsModel.PROMOTION_VERSION_CONTEXT_IDS, new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsModel.REFERENCED_PERMISSIONS, referencedPermissions);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_RELATIONSHIPS_MAPPING,
        new HashMap<>());
    mapToReturn.put(IGetConfigDetailsModel.TYPEID_IDENTIFIER_ATTRIBUTEIDS, new HashMap<>());
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_RELATIONSHIPS_MAPPING,
        new HashMap<String, String>());
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_DATA_RULES,
        referencedDataRuleMap.values());
    mapToReturn.put(IGetConfigDetailsForVersionRollbackModel.REFERENCED_LANGUAGES, new HashMap<>());
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.VERSIONABLE_ATTRIBUTES, new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForVersionRollbackModel.VERSIONABLE_TAGS, new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForVersionRollbackModel.MANDATORY_ATTRIBUTE_IDS,
        new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForVersionRollbackModel.MANDATORY_TAG_IDS, new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForVersionRollbackModel.SHOULD_TAG_IDS, new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForVersionRollbackModel.SHOULD_ATTRIBUTE_IDS,
        new ArrayList<>());
    return mapToReturn;
  }
  
  protected void fillKlassDetails(Map<String, Object> mapToReturn, List<String> klassIds,
      Map<String, Object> referencedDataRuleMap, IGetConfigDetailsHelperModel helperModel,
      String requestedTypeId) throws Exception
  {
    Map<String, Object> referencedKlassMap = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForVersionRollbackModel.REFERENCED_KLASSES);
    for (String klassId : klassIds) {
      try {
        Vertex klassVertex = UtilClass.getVertexById(klassId,
            VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
        if (klassId.equals(requestedTypeId)) {
          helperModel.setRequestedTypeVertex(klassVertex);
          helperModel.setRequestedTypeVertexLabelInfo(VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
        }
        List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
            IKlass.NUMBER_OF_VERSIONS_TO_MAINTAIN, IKlass.ICON, IKlass.LABEL, IKlass.TYPE,
            IKlass.NATURE_TYPE, IKlass.IS_NATURE);
        Map<String, Object> klassMap = UtilClass.getMapFromVertex(fieldsToFetch, klassVertex);
        helperModel.setKlassType(klassVertex.getProperty(IKlass.TYPE));
        Integer numberOfVersionsToMaintain = (Integer) klassMap
            .get(IKlass.NUMBER_OF_VERSIONS_TO_MAINTAIN);
        Integer existingNumberOfVersionsToMaintain = (Integer) mapToReturn
            .get(IGetConfigDetailsForVersionRollbackModel.NUMBER_OF_VERSIONS_TO_MAINTAIN);
        if (numberOfVersionsToMaintain > existingNumberOfVersionsToMaintain) {
          mapToReturn.put(IGetConfigDetailsForVersionRollbackModel.NUMBER_OF_VERSIONS_TO_MAINTAIN,
              numberOfVersionsToMaintain);
        }
        fillReferencedTagsAndLifeCycleStatusTags(mapToReturn, klassVertex);
        fillDataRulesOfKlass(klassVertex, referencedDataRuleMap, helperModel,
            RelationshipLabelConstants.HAS_KLASS_RULE_LINK);
        fillReferencedElements(mapToReturn, klassVertex,
            VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS, null, helperModel, null);
        fillPropertyCouplingInformation(mapToReturn, klassVertex,
            VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS, null, helperModel, null);
        fillEmbeddedReferencedContext(klassVertex, mapToReturn, helperModel);
        Boolean isNature = klassVertex.getProperty(IKlass.IS_NATURE);
        if (isNature != null && isNature) {
          helperModel.setNatureNode(klassVertex);
          fillReferencedNatureRelationships(mapToReturn, klassVertex, helperModel);
        }
        else {
          helperModel.getNonNatureKlassNodes()
              .add(klassVertex);
        }
        referencedKlassMap.put(klassId, klassMap);
      }
      catch (NotFoundException e) {
        
      }
    }
  }
  
  protected void fillTaxonomyDetails(Map<String, Object> mapToReturn, List<String> taxonomyIds,
      Map<String, Object> referencedDataRuleMap, IGetConfigDetailsHelperModel helperModel,
      String requestedTypeId) throws Exception
  {
    Map<String, Object> referencedTaxonomyMap = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForVersionRollbackModel.REFERENCED_TAXONOMIES);
    Set<Vertex> taxonomyVertices = new HashSet<>();
    for (String taxonomyId : taxonomyIds) {
      Vertex taxonomyVertex = null;
      try {
        taxonomyVertex = UtilClass.getVertexByIndexedId(taxonomyId,
            VertexLabelConstants.ROOT_KLASS_TAXONOMY);
        if (taxonomyId.equals(requestedTypeId)) {
          helperModel.setRequestedTypeVertex(taxonomyVertex);
          helperModel.setRequestedTypeVertexLabelInfo(VertexLabelConstants.ROOT_KLASS_TAXONOMY);
        }
        taxonomyVertices.add(taxonomyVertex);
        Map<String, Object> taxonomyMap = UtilClass.getMapFromVertex(
            Arrays.asList(CommonConstants.CODE_PROPERTY, IReferencedArticleTaxonomyModel.LABEL,
                IReferencedArticleTaxonomyModel.ICON, IReferencedArticleTaxonomyModel.TAXONOMY_TYPE,
                IMasterTaxonomyModel.IS_TAXONOMY),
            taxonomyVertex);
        
        /*If its an attribution taxonomy which was deleted, now its just a master tag, then skip it*/
        // Boolean isAttributionTaxonomy = (Boolean)
        // taxonomyMap.remove(IMasterTaxonomyModel.IS_ATTRIBUTION_TAXONOMY);
        
        Boolean isAttributionTaxonomy = taxonomyVertex
            .getProperty(CommonConstants.ORIENTDB_CLASS_PROPERTY)
            .equals(VertexLabelConstants.ATTRIBUTION_TAXONOMY);
        Boolean isTaxonomy = (Boolean) taxonomyMap.remove(IMasterTaxonomyModel.IS_TAXONOMY);
        if (
        /*isAttributionTaxonomy != null &&*/ isAttributionTaxonomy) {
          if (
          /*isTaxonomy != null && */ !isTaxonomy) {
            continue;
          }
        }
        fillTaxonomiesChildrenAndParentData(taxonomyMap, taxonomyVertex);
        fillDataRulesOfKlass(taxonomyVertex, referencedDataRuleMap, helperModel,
            RelationshipLabelConstants.HAS_TAXONOMY_RULE_LINK);
        fillReferencedElements(mapToReturn, taxonomyVertex,
            VertexLabelConstants.ROOT_KLASS_TAXONOMY, null, helperModel, null);
        fillPropertyCouplingInformation(mapToReturn, taxonomyVertex,
            VertexLabelConstants.ROOT_KLASS_TAXONOMY, null, helperModel, null);
        fillEmbeddedReferencedContext(taxonomyVertex, mapToReturn, helperModel);
        referencedTaxonomyMap.put(taxonomyId, taxonomyMap);
      }
      catch (NotFoundException e) {
        
      }
    }
    helperModel.setTaxonomyVertices(taxonomyVertices);
  }
  
  /**
   * Description: Fill property coupling information (unresolved references) for
   * Attribute and Tags.
   *
   * @author Niraj
   * @param mapToReturn
   * @param klassVertex
   * @param vertexLabel
   * @param contextId
   * @param helperModel
   * @param propertiesToFetch
   * @throws Exception
   */
  protected void fillPropertyCouplingInformation(Map<String, Object> mapToReturn,
      Vertex klassVertex, String vertexLabel, String contextId,
      IGetConfigDetailsHelperModel helperModel, List<String> propertiesToFetch) throws Exception
  {
    Map<String, Object> referencedPropertyCouplingInformation = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForVersionRollbackModel.REFERENCED_PROPERTY_COUPLING_INFORMATION);
    String typeId = UtilClass.getCodeNew(klassVertex);
    
    Iterable<Vertex> kPNodesIterable = null;
    if (propertiesToFetch == null || propertiesToFetch.isEmpty()) {
      kPNodesIterable = klassVertex.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_KLASS_PROPERTY);
    }
    else {
      String query = "SELECT FROM (SELECT EXPAND (OUT('"
          + RelationshipLabelConstants.HAS_KLASS_PROPERTY + "')) FROM " + klassVertex.getId()
          + " ) WHERE " + ISectionElement.PROPERTY_ID + " IN "
          + EntityUtil.quoteIt(propertiesToFetch);
      kPNodesIterable = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
    }
    
    for (Vertex klassPropertyNode : kPNodesIterable) {
      Map<String, Object> couplingInformation = new HashMap<>();
      String entityType = klassPropertyNode.getProperty(ISectionElement.TYPE);
      String propertyId = klassPropertyNode.getProperty(ISectionElement.PROPERTY_ID);
      
      if (entityType.equals(CommonConstants.ATTRIBUTE) || entityType.equals(CommonConstants.TAG)) {
        
        if (entityType.equals(CommonConstants.TAG)) {
          List<Map<String, Object>> defaultTagValues = KlassUtils
              .getDefaultTagValuesOfKlassPropertyNode(klassPropertyNode);
          couplingInformation.put(IPropertyCouplingInformationModel.DEFAULT_VALUE,
              defaultTagValues);
        }
        else if (entityType.equals(CommonConstants.ATTRIBUTE)) {
          couplingInformation.put(IPropertyCouplingInformationModel.DEFAULT_VALUE,
              klassPropertyNode.getProperty(ISectionAttribute.DEFAULT_VALUE));
        }
        
        Map<String, Object> propertyCouplingInformation = new HashMap<>();
        if (referencedPropertyCouplingInformation.containsKey(propertyId)) {
          propertyCouplingInformation = (Map<String, Object>) referencedPropertyCouplingInformation
              .get(propertyId);
        }
        referencedPropertyCouplingInformation.put(propertyId, propertyCouplingInformation);
        couplingInformation.put(IPropertyCouplingInformationModel.COUPLING_TYPE,
            klassPropertyNode.getProperty(ISectionElement.COUPLING_TYPE));
        propertyCouplingInformation.put(typeId, couplingInformation);
      }
    }
  }
}
