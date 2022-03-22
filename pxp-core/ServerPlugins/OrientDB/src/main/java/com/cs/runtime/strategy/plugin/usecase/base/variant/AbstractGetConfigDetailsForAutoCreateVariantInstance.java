package com.cs.runtime.strategy.plugin.usecase.base.variant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
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
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variantcontext.util.VariantContextUtils;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.propertycollection.ISectionAttribute;
import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;
import com.cs.core.config.interactor.entity.propertycollection.ISectionTag;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.exception.context.ContextNotFoundException;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextTagsModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCreateVariantModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.core.runtime.interactor.model.variants.IConfigDetailsForGetVariantInstancesInTableViewModel;
import com.cs.runtime.strategy.plugin.usecase.base.AbstractGetConfigDetails;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings("unchecked")
public abstract class AbstractGetConfigDetailsForAutoCreateVariantInstance
    extends AbstractGetConfigDetails {
  
  public AbstractGetConfigDetailsForAutoCreateVariantInstance(
      final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    return getConfigDetails(requestMap, null);
  }
  
  /**
   * @author Lokesh
   * @param requestMap
   * @param nodeLabel
   * @return
   * @throws Exception
   */
  protected Map<String, Object> getConfigDetails(Map<String, Object> requestMap, String contextId) throws Exception
  {
    if(contextId == null) {
      contextId = (String) requestMap.get(IMulticlassificationRequestModel.CONTEXT_ID);
    }
    
    Map<String, Object> referencedDataRuleMap = new HashMap<>();
    Map<String, Object> mapToReturn = getMapToReturn(referencedDataRuleMap);
    
    IGetConfigDetailsHelperModel helperModel = new GetConfigDetailsHelperModel();
    helperModel
        .setEndpointId((String) requestMap.get(IMulticlassificationRequestModel.ENDPOINT_ID));
    helperModel.setOrganizationId(
        (String) requestMap.get(IMulticlassificationRequestModel.ORAGANIZATION_ID));
    helperModel.setPhysicalCatalogId(
        (String) requestMap.get(IMulticlassificationRequestModel.PHYSICAL_CATALOG_ID));
    Vertex contextNode = null;
    try {
      contextNode = UtilClass.getVertexById(contextId, VertexLabelConstants.VARIANT_CONTEXT);
    }
    catch (NotFoundException e) {
      throw new ContextNotFoundException();
    }
    String baseType = (String) requestMap.get(IMulticlassificationRequestModel.BASE_TYPE);
    fillContextAndKlassDetails(contextNode, mapToReturn, helperModel, referencedDataRuleMap,
        baseType);
    fillReferencedElementInRespectiveMap(mapToReturn, helperModel);
    List<String> parentTaxonomyIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.PARENT_TAXONOMY_IDS);
    List<String> parentKlassIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.PARENT_KLASS_IDS);
    mergeCouplingTypeOfReferencedElementsFromContext(mapToReturn, helperModel.getNatureNode(),
        parentKlassIds, parentTaxonomyIds);
    return mapToReturn;
  }
  
  protected void fillReferencedPermission(Map<String, Object> responseMap, Vertex userInRole,
      IGetConfigDetailsHelperModel helperModel) throws Exception
  {
    fillKlassIdsAndTaxonomyIdsHavingReadPermission(userInRole, responseMap);
    Map<String, Object> referencedPermissions = (Map<String, Object>) responseMap
        .get(IGetConfigDetailsModel.REFERENCED_PERMISSIONS);
    String roleId = UtilClass.getCodeNew(userInRole);
    
    fillNatureKlassPermissions(responseMap, helperModel, referencedPermissions, roleId);
  }
  
  private void fillContextAndKlassDetails(Vertex contextNode, Map<String, Object> mapToReturn,
      IGetConfigDetailsHelperModel helperModel, Map<String, Object> referencedDataRuleMap,
      String baseType) throws Exception
  {
    Iterator<Vertex> iterator = contextNode
        .getVertices(Direction.IN, RelationshipLabelConstants.VARIANT_CONTEXT_OF)
        .iterator();
    Vertex klassVertex = VariantContextUtils.getKlassNodeFromContextNode(baseType, iterator);
    helperModel.setNatureNode(klassVertex);
    List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
        IKlass.NUMBER_OF_VERSIONS_TO_MAINTAIN, IKlass.ICON, IKlass.LABEL, IKlass.TYPE,
        IKlass.NATURE_TYPE, IKlass.IS_NATURE);
    Map<String, Object> klassMap = UtilClass.getMapFromVertex(fieldsToFetch, klassVertex);
    
    fillReferencedElements(mapToReturn, klassVertex, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS,
        null, helperModel, null);
    fillReferencedPropertyCollections(helperModel, klassVertex, mapToReturn, null);
    
    Map<String, Object> referencedKlassMap = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_KLASSES);
    String klassId = UtilClass.getCodeNew(klassVertex);
    referencedKlassMap.put(klassId, klassMap);
    int numberOfVersionsToMaintain = (int) klassMap.get(IGetConfigDetailsForCreateVariantModel.NUMBER_OF_VERSIONS_TO_MAINTAIN);
    mapToReturn.put(IGetConfigDetailsForCreateVariantModel.NUMBER_OF_VERSIONS_TO_MAINTAIN, numberOfVersionsToMaintain);
    
    Map<String, Object> referencedPropertyCollections = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_PROPERTY_COLLECTIONS);
    
    fillReferencedVariantContext(contextNode, mapToReturn, klassId, referencedPropertyCollections);
    fillDataRulesOfKlass(klassVertex, referencedDataRuleMap, helperModel,
        RelationshipLabelConstants.HAS_KLASS_RULE_LINK);
  }
  
  private void fillReferencedVariantContext(Vertex contextNode, Map<String, Object> mapToReturn,
      String klassId, Map<String, Object> referencedPropertyCollections) throws Exception
  {
    Map<String, Object> referencedVariantContexts = (Map<String, Object>) mapToReturn
        .get(IConfigDetailsForGetVariantInstancesInTableViewModel.REFERENCED_VARIANT_CONTEXTS);
    Map<String, Object> referencedEmbeddedVariantContexts = (Map<String, Object>) referencedVariantContexts
        .get(IReferencedContextModel.EMBEDDED_VARIANT_CONTEXTS);
    Map<String, Object> referencedTagsMap = (Map<String, Object>) mapToReturn
        .get(IConfigDetailsForGetVariantInstancesInTableViewModel.REFERENCED_TAGS);
    
    Map<String, Object> variantContextMap = VariantContextUtils.getReferencedContexts(contextNode);
    
    variantContextMap.put(IReferencedVariantContextModel.PROPERTY_COLLECTIONS,
        referencedPropertyCollections.keySet());
    
    variantContextMap.put(IReferencedVariantContextModel.CONTEXT_KLASS_ID, klassId);
    
    for (Map<String, Object> contextTag : (List<Map<String, Object>>) variantContextMap
        .get(IReferencedVariantContextModel.TAGS)) {
      String entityId = (String) contextTag.get(IReferencedVariantContextTagsModel.TAG_ID);
      if (referencedTagsMap.containsKey(entityId)) {
        continue;
      }
      Vertex entityNode = UtilClass.getVertexById(entityId, VertexLabelConstants.ENTITY_TAG);
      Map<String, Object> entity = TagUtils.getTagMap(entityNode, false);
      referencedTagsMap.put(entityId, entity);
    }
    referencedEmbeddedVariantContexts.put(UtilClass.getCodeNew(contextNode), variantContextMap);
  }
  
  protected Map<String, Object> getMapToReturn(Map<String, Object> referencedDataRuleMap)
  {
    
    Map<String, Object> mapToReturn = new HashMap<>();
    Map<String, Object> referencedSectionElementMap = new HashMap<>();
    Map<String, Object> referencedAttributeMap = new HashMap<>();
    Map<String, Object> referencedTagMap = new HashMap<>();
    Map<String, Object> referencedRolesMap = new HashMap<>();
    Map<String, Object> referencedKlasses = new HashMap<String, Object>();
    Map<String, Object> referencedPropertyCollectionMap = new HashMap<>();
    List<String> referencedLifeCycleStatusTags = new ArrayList<>();
    Map<String, Object> referencedVariantContexts = new HashMap<>();
    
    Map<String, Object> embeddedVariantContexts = new HashMap<>();
    Map<String, Object> productVariantContexts = new HashMap<>();
    Map<String, Object> languageVariantContexts = new HashMap<>();
    
    referencedVariantContexts.put(IReferencedContextModel.EMBEDDED_VARIANT_CONTEXTS,
        embeddedVariantContexts);
    referencedVariantContexts.put(IReferencedContextModel.LANGUAGE_VARIANT_CONTEXTS,
        languageVariantContexts);
    referencedVariantContexts.put(IReferencedContextModel.PRODUCT_VARIANT_CONTEXTS,
        productVariantContexts);
    
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_DATA_RULES,
        referencedDataRuleMap.values());
    mapToReturn.put(IGetConfigDetailsForCreateVariantModel.REFERENCED_ELEMENTS,
        referencedSectionElementMap);
    mapToReturn.put(IGetConfigDetailsForCreateVariantModel.REFERENCED_ATTRIBUTES,
        referencedAttributeMap);
    mapToReturn.put(IGetConfigDetailsForCreateVariantModel.REFERENCED_TAGS, referencedTagMap);
    mapToReturn.put(IGetConfigDetailsForCreateVariantModel.REFERENCED_ROLES, referencedRolesMap);
    mapToReturn.put(IGetConfigDetailsForCreateVariantModel.REFERENCED_PROPERTY_COLLECTIONS,
        referencedPropertyCollectionMap);
    mapToReturn.put(IGetConfigDetailsForCreateVariantModel.REFERENCED_LIFECYCLE_STATUS_TAGS,
        referencedLifeCycleStatusTags);
    mapToReturn.put(IGetConfigDetailsForCreateVariantModel.REFRENCED_KLASSES, referencedKlasses);
    mapToReturn.put(IGetConfigDetailsForCreateVariantModel.REFERENCED_VARIANT_CONTEXTS,
        referencedVariantContexts);
    
    return mapToReturn;
  }
  
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
        
        referencedTags.put(entityId, entity);
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
  protected void fillReferencedElements(Map<String, Object> mapToReturn, Vertex klassVertex,
      String vertexLabel, String contextId, IGetConfigDetailsHelperModel helperModel,
      List<String> propertiesToFetch) throws Exception
  {
    Map<String, Object> referencedElements = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_ELEMENTS);
    Map<String, Set<String>> typeIdVsAssociatedPropertyIds = helperModel
        .getTypeIdVsAssociatedPropertyIds();
    Set<String> propertyIds = new HashSet<>();
    typeIdVsAssociatedPropertyIds.put(UtilClass.getCodeNew(klassVertex), propertyIds);
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
      String entityId = klassPropertyNode.getProperty(ISectionElement.PROPERTY_ID);
      propertyIds.add(entityId);
      String klassPropertyContextId = null;
      Iterator<Vertex> attributeContextsIterator = klassPropertyNode
          .getVertices(Direction.IN, RelationshipLabelConstants.VARIANT_CONTEXT_OF)
          .iterator();
      if (contextId != null && !attributeContextsIterator.hasNext()) {
        continue;
      }
      
      if (attributeContextsIterator.hasNext()) {
        Vertex attributeContextVertex = attributeContextsIterator.next();
        klassPropertyContextId = UtilClass.getCodeNew(attributeContextVertex);
        if (contextId != null && !contextId.equals(klassPropertyContextId)) {
          continue;
        }
        Map<String, Object> referencedVariantContexts = (Map<String, Object>) mapToReturn
            .get(IGetConfigDetailsForCustomTabModel.REFERENCED_VARIANT_CONTEXTS);
        Map<String, Object> embeddedContexts = (Map<String, Object>) referencedVariantContexts
            .get(IReferencedContextModel.EMBEDDED_VARIANT_CONTEXTS);
        helperModel.getAssociatedAttributeContextIds()
            .add(klassPropertyContextId);
        if (embeddedContexts.containsKey(klassPropertyContextId)) {
          Map<String, Object> propertyContextMap = (Map<String, Object>) embeddedContexts
              .get(klassPropertyContextId);
          Set<String> entityIds = (Set<String>) propertyContextMap
              .get(IReferencedVariantContextModel.ENTITY_IDS);
          entityIds.add(entityId);
        }
        else {
          Map<String, Object> propertyContextMap = UtilClass
              .getMapFromVertex(PROPERTY_CONTEXT_FIELDS_TO_FETCH, attributeContextVertex);
          Set<String> entityIds = new HashSet<>();
          entityIds.add(entityId);
          propertyContextMap.put(IReferencedVariantContextModel.ENTITY_IDS, entityIds);
          embeddedContexts.put(klassPropertyContextId, propertyContextMap);
        }
      }
      
      String entityType = klassPropertyNode.getProperty(CommonConstants.TYPE);
      
      Map<String, Object> referencedElementMap = UtilClass.getMapFromNode(klassPropertyNode);
      referencedElementMap.put(CommonConstants.ID_PROPERTY, entityId);
      if (klassPropertyContextId != null) {
        referencedElementMap.put(ISectionAttribute.ATTRIBUTE_VARIANT_CONTEXT,
            klassPropertyContextId);
      }
      
      switch (entityType) {
        case SystemLevelIds.PROPERTY_TYPE_TAG:
          List<Map<String, Object>> defaultTagValues = KlassUtils
              .getDefaultTagValuesOfKlassPropertyNode(klassPropertyNode);
          referencedElementMap.put(ISectionTag.DEFAULT_VALUE, defaultTagValues);
          List<String> selectedTagValues = KlassUtils
              .getSelectedTagValuesListOfKlassPropertyNode(klassPropertyNode);
          referencedElementMap.put(CommonConstants.SELECTED_TAG_VALUES_LIST, selectedTagValues);
          helperModel.getTagIds()
              .add(entityId);
          break;
        
        case SystemLevelIds.PROPERTY_TYPE_ATTRIBUTE:
          helperModel.getAttributeIds()
              .add(entityId);
          break;
        
        case SystemLevelIds.PROPERTY_TYPE_ROLE:
          helperModel.getRoleIds()
              .add(entityId);
          break;
        default:
          continue;
      }
      if (referencedElements.containsKey(entityId)) {
        Map<String, Object> existingReferencedElement = (Map<String, Object>) referencedElements
            .get(entityId);
        mergeReferencedElement(referencedElementMap, existingReferencedElement,
            UtilClass.getCodeNew(klassVertex), entityType, true);
      }
      else {
        referencedElements.put(entityId, referencedElementMap);
      }
    }
  }
}
