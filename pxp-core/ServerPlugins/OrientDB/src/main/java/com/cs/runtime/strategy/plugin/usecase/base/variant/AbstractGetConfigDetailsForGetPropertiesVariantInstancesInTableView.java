package com.cs.runtime.strategy.plugin.usecase.base.variant;

import com.cs.config.strategy.plugin.model.GetConfigDetailsHelperModel;
import com.cs.config.strategy.plugin.model.IGetConfigDetailsHelperModel;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variant.util.VariantUtils;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.attribute.INumberAttribute;
import com.cs.core.config.interactor.entity.attribute.IUnitAttribute;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.exception.context.ContextNotFoundException;
import com.cs.core.config.interactor.model.taxonomy.IApplicableFilterModel;
import com.cs.core.config.interactor.model.taxonomy.ISortDataModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.ITemplatePermissionForGetVariantInstancesModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInfoModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.core.runtime.interactor.model.variants.IConfigDetailsForGetPropertiesVariantInstancesInTableViewModel;
import com.cs.core.runtime.interactor.model.variants.IConfigDetailsForGetPropertiesVariantInstancesInTableViewStrategyRequestModel;
import com.cs.core.runtime.interactor.model.variants.IConfigDetailsForGetVariantInstancesInTableViewModel;
import com.cs.runtime.strategy.plugin.usecase.base.AbstractGetConfigDetails;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public abstract class AbstractGetConfigDetailsForGetPropertiesVariantInstancesInTableView
    extends AbstractGetConfigDetails {
  
  public AbstractGetConfigDetailsForGetPropertiesVariantInstancesInTableView(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  protected static final List<String> FIELDS_TO_FETCH_FOR_FILTER_ATTRIBUTE_DATA = Arrays.asList(
      IAttribute.ID, IAttribute.LABEL, IAttribute.TYPE, IUnitAttribute.DEFAULT_UNIT,
      INumberAttribute.PRECISION, IAttribute.CODE);
  
  protected static final List<String> FIELDS_TO_FETCH_FOR_FILTER_TAG_DATA       = Arrays
      .asList(ITag.ID, ITag.LABEL, ITag.CODE);
  
  protected static final List<String> FIELDS_TO_FETCH_FOR_SORT_DATA             = Arrays
      .asList(IAttribute.ID, IAttribute.TYPE, IAttribute.LABEL);
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String userId = (String) requestMap.get(
        IConfigDetailsForGetPropertiesVariantInstancesInTableViewStrategyRequestModel.CURRENT_USER_ID);
    String contextId = (String) requestMap.get(
        IConfigDetailsForGetPropertiesVariantInstancesInTableViewStrategyRequestModel.CONTEXT_ID);
    String attributeId = (String) requestMap.get(
        IConfigDetailsForGetPropertiesVariantInstancesInTableViewStrategyRequestModel.ATTRIBUTE_ID);
    List<String> klassIds = (List<String>) requestMap.get(
        IConfigDetailsForGetPropertiesVariantInstancesInTableViewStrategyRequestModel.KLASS_IDS);
    List<String> taxonomyIds = (List<String>) requestMap.get(
        IConfigDetailsForGetPropertiesVariantInstancesInTableViewStrategyRequestModel.TAXONOMY_IDS);
    Vertex userInRole = RoleUtils.getRoleFromUser(userId);
    
    Map<String, Object> mapToReturn = getMapToReturn();
    
    fillEntityIdsHavingReadPermission(userInRole, mapToReturn);
    
    IGetConfigDetailsHelperModel helperModel = new GetConfigDetailsHelperModel();
    helperModel.setUserId(userId);
    ArrayList<String> propertiesToFetch = new ArrayList<>();
    propertiesToFetch.add(attributeId);
    
    for (String klassId : klassIds) {
      Vertex klassVertex = null;
      try {
        klassVertex = UtilClass.getVertexById(klassId, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
      
      String natureType = klassVertex.getProperty(IKlass.NATURE_TYPE);
      if (natureType != null && !natureType.isEmpty()) {
        helperModel.setNatureNode(klassVertex);
      }
      else {
        helperModel.getNonNatureKlassNodes()
            .add(klassVertex);
      }
      fillReferencedPropertyCollections(helperModel, klassVertex, mapToReturn, propertiesToFetch);
      fillReferencedElements(mapToReturn, klassVertex, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS,
          contextId, helperModel, propertiesToFetch);
    }
    
    for (String taxonomyId : taxonomyIds) {
      Vertex taxonomyVertex = null;
      try {
        taxonomyVertex = UtilClass.getVertexById(taxonomyId,
            VertexLabelConstants.ROOT_KLASS_TAXONOMY);
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
      
      helperModel.getTaxonomyVertices()
          .add(taxonomyVertex);
      fillReferencedPropertyCollections(helperModel, taxonomyVertex, mapToReturn,
          propertiesToFetch);
      fillReferencedElements(mapToReturn, taxonomyVertex,
          VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS, contextId, helperModel, propertiesToFetch);
    }
    
    fillFilterInfo(mapToReturn, contextId);
    fillReferencedPermission(mapToReturn, userInRole, helperModel);
    
    fillReferencedElementInRespectiveMap(mapToReturn, helperModel);
    filterOutReferencedElements(mapToReturn, helperModel);
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
    fillNonNatureKlassPermissions(responseMap, helperModel, referencedPermissions, roleId);
    fillTaxonomyPermissions(responseMap, helperModel, referencedPermissions, roleId);
  }
  
  /**
   * fill searchData , sort Data and filterData using referencedAttributes,
   * referencedTags and referencedElements
   *
   * @author Lokesh
   * @param mapToReturn
   * @param contextId
   * @throws ContextNotFoundException
   */
  protected void fillFilterInfo(Map<String, Object> mapToReturn, String contextId)
      throws ContextNotFoundException
  {
    Map<String, Object> referencedAttributes = (Map<String, Object>) mapToReturn
        .get(IConfigDetailsForGetPropertiesVariantInstancesInTableViewModel.REFERENCED_ATTRIBUTES);
    Map<String, Object> referencedTags = (Map<String, Object>) mapToReturn
        .get(IConfigDetailsForGetPropertiesVariantInstancesInTableViewModel.REFERENCED_TAGS);
    
    List<String> searchableAttributes = new ArrayList<>();
    List<Map<String, Object>> sortableAttributes = new ArrayList<>();
    List<Map<String, Object>> filterData = new ArrayList<>();
    
    for (String attributeId : referencedAttributes.keySet()) {
      Map<String, Object> referencedAttribute = (Map<String, Object>) referencedAttributes
          .get(attributeId);
      Map<String, Object> sortableMap = prepareMapByFieldsToPut(referencedAttribute,
          FIELDS_TO_FETCH_FOR_SORT_DATA);
      Map<String, Object> filterMap = prepareMapByFieldsToPut(referencedAttribute,
          FIELDS_TO_FETCH_FOR_FILTER_ATTRIBUTE_DATA);
      searchableAttributes.add(attributeId);
      sortableAttributes.add(sortableMap);
      filterData.add(filterMap);
    }
    
    Map<String, Object> referencedContextModel = (Map<String, Object>) mapToReturn
        .get(IConfigDetailsForGetVariantInstancesInTableViewModel.REFERENCED_VARIANT_CONTEXTS);
    Map<String, Object> referencedVariantContext = (Map<String, Object>) referencedContextModel
        .get(IReferencedContextModel.EMBEDDED_VARIANT_CONTEXTS);
    Map<String, Object> contextMap = (Map<String, Object>) referencedVariantContext.get(contextId);
    
    if (contextMap == null) {
      throw new ContextNotFoundException();
    }
    
    List<String> contextTagIds = VariantUtils.getContextTagIds(contextMap);
    
    for (String tagId : referencedTags.keySet()) {
      if (!contextTagIds.contains(tagId)) {
        continue;
      }
      Map<String, Object> referencedTag = (Map<String, Object>) referencedTags.get(tagId);
      Map<String, Object> filterMap = prepareMapByFieldsToPut(referencedTag,
          FIELDS_TO_FETCH_FOR_FILTER_TAG_DATA);
      List<Map<String, Object>> children = new ArrayList<>();
      filterMap.put(IApplicableFilterModel.TYPE, referencedTag.get(ITag.TAG_TYPE));
      filterMap.put(IApplicableFilterModel.CHILDREN, children);
      List<Map<String, Object>> tagValues = (List<Map<String, Object>>) referencedTag
          .get(ITag.CHILDREN);
      for (Map<String, Object> tagValue : tagValues) {
        Map<String, Object> tagValueMap = prepareMapByFieldsToPut(tagValue,
            FIELDS_TO_FETCH_FOR_FILTER_TAG_DATA);
        tagValueMap.remove(ITag.TAG_TYPE);
        children.add(tagValueMap);
      }
      filterData.add(filterMap);
    }
    
    Map<String, Object> sortData = new HashMap<String, Object>();
    sortData.put(ISortDataModel.ATTRIBUTES, sortableAttributes);
    /*Map<String, Object> filterData = new HashMap<String, Object>();
    filterData.put(IFilterDataModel.ATTRIBUTES, filterableAttributes);
    filterData.put(IFilterDataModel.TAGS, filterableTags);*/
    
    Map<String, Object> filterInfo = new HashMap<>();
    filterInfo.put(IGetFilterInfoModel.SEARCHABLE_ATTRIBUTES, searchableAttributes);
    filterInfo.put(IGetFilterInfoModel.SORT_DATA, sortData);
    filterInfo.put(IGetFilterInfoModel.FILTER_DATA, filterData);
    
    mapToReturn.put(IConfigDetailsForGetPropertiesVariantInstancesInTableViewModel.FILTER_INFO,
        filterInfo);
  }
  
  private Map<String, Object> prepareMapByFieldsToPut(Map<String, Object> requestMap,
      List<String> fieldsToPut)
  {
    Map<String, Object> responseMap = new HashMap<String, Object>();
    for (String field : fieldsToPut) {
      if (!requestMap.containsKey(field)) {
        continue;
      }
      responseMap.put(field, requestMap.get(field));
    }
    return responseMap;
  }
  
  protected Map<String, Object> getMapToReturn()
  {
    
    Map<String, Object> mapToReturn = new HashMap<>();
    
    Map<String, Object> referencedPropertyCollectionMap = new HashMap<>();
    Map<String, Object> referencedSectionElementMap = new HashMap<>();
    Map<String, Object> referencedAttributeMap = new HashMap<>();
    Map<String, Object> referencedTagMap = new HashMap<>();
    Map<String, Object> referencedRolesMap = new HashMap<>();
    
    Map<String, Object> referencedVariantContextsMap = new HashMap<>();
    Map<String, Object> embeddedVariantContexts = new HashMap<>();
    referencedVariantContextsMap.put(IReferencedContextModel.EMBEDDED_VARIANT_CONTEXTS,
        embeddedVariantContexts);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_VARIANT_CONTEXTS,
        referencedVariantContextsMap);
    
    Map<String, Object> referencedPermissions = new HashMap<>();
    referencedPermissions.put(
        ITemplatePermissionForGetVariantInstancesModel.EDITABLE_PROPERTY_COLLECTION_IDS,
        new HashSet<String>());
    referencedPermissions.put(
        ITemplatePermissionForGetVariantInstancesModel.EXPANDABLE_PROPERTY_COLLECTION_IDS,
        new HashSet<String>());
    referencedPermissions.put(
        ITemplatePermissionForGetVariantInstancesModel.VISIBLE_PROPERTY_COLLECTION_IDS,
        new HashSet<String>());
    referencedPermissions.put(ITemplatePermissionForGetVariantInstancesModel.EDITABLE_PROPERTY_IDS,
        new HashSet<String>());
    referencedPermissions.put(ITemplatePermissionForGetVariantInstancesModel.VISIBLE_PROPERTY_IDS,
        new HashSet<String>());
    referencedPermissions.put(ITemplatePermissionForGetVariantInstancesModel.ENTITIES_HAVING_RP,
        new HashSet<String>());
    referencedPermissions.put(ITemplatePermissionForGetVariantInstancesModel.KLASS_IDS_HAVING_RP,
        new HashSet<String>());
    referencedPermissions.put(ITemplatePermissionForGetVariantInstancesModel.TAXONOMY_IDS_HAVING_RP,
        new HashSet<String>());
    referencedPermissions.put(
        ITemplatePermissionForGetVariantInstancesModel.ALL_TAXONOMY_IDS_HAVING_RP,
        new HashSet<String>());
    mapToReturn.put(
        IConfigDetailsForGetPropertiesVariantInstancesInTableViewModel.REFERENCED_PROPERTY_COLLECTIONS,
        referencedPropertyCollectionMap);
    mapToReturn.put(
        IConfigDetailsForGetPropertiesVariantInstancesInTableViewModel.REFERENCED_VARIANT_CONTEXTS,
        referencedVariantContextsMap);
    mapToReturn.put(
        IConfigDetailsForGetPropertiesVariantInstancesInTableViewModel.REFERENCED_ELEMENTS,
        referencedSectionElementMap);
    mapToReturn.put(
        IConfigDetailsForGetPropertiesVariantInstancesInTableViewModel.REFERENCED_ATTRIBUTES,
        referencedAttributeMap);
    mapToReturn.put(IConfigDetailsForGetPropertiesVariantInstancesInTableViewModel.REFERENCED_TAGS,
        referencedTagMap);
    mapToReturn.put(IConfigDetailsForGetPropertiesVariantInstancesInTableViewModel.REFERENCED_ROLES,
        referencedRolesMap);
    mapToReturn.put(
        IConfigDetailsForGetPropertiesVariantInstancesInTableViewModel.REFERENCED_PERMISSIONS,
        referencedPermissions);
    mapToReturn.put(
        IConfigDetailsForGetPropertiesVariantInstancesInTableViewModel.TYPEID_IDENTIFIER_ATTRIBUTEIDS,
        new HashMap<>());
    mapToReturn.put(
        IConfigDetailsForGetPropertiesVariantInstancesInTableViewModel.VERSIONABLE_ATTRIBUTES,
        new ArrayList<>());
    mapToReturn.put(IConfigDetailsForGetPropertiesVariantInstancesInTableViewModel.VERSIONABLE_TAGS,
        new ArrayList<>());
    
    mapToReturn.put(
        IConfigDetailsForGetPropertiesVariantInstancesInTableViewModel.MANDATORY_ATTRIBUTE_IDS,
        new ArrayList<>());
    mapToReturn.put(
        IConfigDetailsForGetPropertiesVariantInstancesInTableViewModel.MANDATORY_TAG_IDS,
        new ArrayList<>());
    mapToReturn.put(IConfigDetailsForGetPropertiesVariantInstancesInTableViewModel.SHOULD_TAG_IDS,
        new ArrayList<>());
    mapToReturn.put(
        IConfigDetailsForGetPropertiesVariantInstancesInTableViewModel.SHOULD_ATTRIBUTE_IDS,
        new ArrayList<>());
    
    return mapToReturn;
  }
  
  @Override
  protected void fillNatureKlassPermissions(Map<String, Object> responseMap,
      IGetConfigDetailsHelperModel helperModel, Map<String, Object> referencedPermissions,
      String roleId) throws Exception
  {
    Vertex natureKlassVertex = helperModel.getNatureNode();
    fillPropertyCollectionPermissionAndEntitiesPermission(responseMap, helperModel,
        referencedPermissions, natureKlassVertex, roleId);
  }
  
  @Override
  protected void fillNonNatureKlassPermissions(Map<String, Object> responseMap,
      IGetConfigDetailsHelperModel helperModel, Map<String, Object> referencedPermissions,
      String roleId) throws Exception
  {
    Set<Vertex> nonNatureKlassVertices = helperModel.getNonNatureKlassNodes();
    for (Vertex nonNatureKlassVertex : nonNatureKlassVertices) {
      fillPropertyCollectionPermissionAndEntitiesPermission(responseMap, helperModel,
          referencedPermissions, nonNatureKlassVertex, roleId);
    }
  }
}
