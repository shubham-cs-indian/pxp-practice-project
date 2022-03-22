package com.cs.config.strategy.plugin.usecase.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.cs.config.standard.IStandardConfig;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.attributiontaxonomy.IMasterTaxonomy;
import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.config.interactor.entity.mapping.IMapping;
import com.cs.core.config.interactor.entity.processevent.IProcessEvent;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollection;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.task.ITask;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.entity.user.IUser;
import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.config.interactor.exception.tag.TagNotFoundException;
import com.cs.core.config.interactor.model.attributiontaxonomy.IMasterTaxonomyModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEndpointPaginationModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityPaginationModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityResponseModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataPropertyCollectionPaginationModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataResponseModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataTagValuesPaginationModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataWorkflowPaginationModel;
import com.cs.core.config.interactor.model.translations.IStandardTranslationModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.di.config.interactor.model.initializeworflowevent.ISaveProcessEventModel;
import com.cs.plugin.utility.klassproperty.klasstag.KlassTagUtil;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.cs.workflow.base.WorkflowType;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings("unchecked")
public class GetConfigData extends AbstractOrientPlugin {
  
  private static final List<String> FIELDS_TO_FETCH               = Arrays.asList(
      CommonConstants.ICON_PROPERTY, CommonConstants.LABEL_PROPERTY, CommonConstants.TYPE_PROPERTY,
      CommonConstants.CODE_PROPERTY);
  
  private static final List<String> FIELDS_TO_FETCH_FOR_ATTRIBUTE = Arrays.asList(
      CommonConstants.LABEL_PROPERTY, CommonConstants.TYPE_PROPERTY, IAttribute.ICON,
      CommonConstants.CODE_PROPERTY);
  
  private static final List<String> FIELDS_TO_FETCH_FOR_TAG       = Arrays.asList(
      CommonConstants.LABEL_PROPERTY, CommonConstants.TYPE_PROPERTY, ITag.ICON, ITag.COLOR,
      ITag.IS_MULTI_SELECT, CommonConstants.CODE_PROPERTY, ITag.TAG_TYPE);
  
  private static final List<String> FIELDS_TO_FETCH_FOR_PCS       = Arrays.asList(
      CommonConstants.LABEL_PROPERTY, IPropertyCollection.IS_FOR_X_RAY,
      CommonConstants.CODE_PROPERTY, IPropertyCollection.ICON);
  
  private static final List<String> FIELDS_TO_FETCH_FOR_USER      = Arrays.asList(
      CommonConstants.ICON_PROPERTY, CommonConstants.LABEL_PROPERTY, CommonConstants.TYPE_PROPERTY,
      CommonConstants.CODE_PROPERTY, IUser.FIRST_NAME, IUser.LAST_NAME, IUser.USER_IID, IUser.USER_NAME);
  
  public static final String        IS_ATTRIBUTE_CONTEXT          = "isAttributeContext";
  
  private static final List<String> FIELDS_TO_FETCH_FOR_KPI       = Arrays.asList(
      CommonConstants.LABEL_PROPERTY, CommonConstants.TYPE_PROPERTY, CommonConstants.ICON_PROPERTY,
      CommonConstants.CODE_PROPERTY);
  
  private static final List<String> FIELDS_TO_FETCH_FOR_TAXONOMY  = Arrays.asList(CommonConstants.ICON_PROPERTY,
      CommonConstants.LABEL_PROPERTY, CommonConstants.BASE_TYPE, CommonConstants.CODE_PROPERTY);
  
  private static final List<String> FIELDS_TO_FETCH_FOR_PROCESS              = Arrays.asList(
      CommonConstants.ICON_PROPERTY, CommonConstants.LABEL_PROPERTY, CommonConstants.TYPE_PROPERTY,
      CommonConstants.CODE_PROPERTY, IProcessEvent.PROCESS_DEFINITION_ID);
  
  public GetConfigData(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigData/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String searchColumn = (String) requestMap.get(IGetConfigDataRequestModel.SEARCH_COLUMN);
    String searchText = (String) requestMap.get(IGetConfigDataRequestModel.SEARCH_TEXT);
    searchText = searchText.replace("'", "\\'");
    Map<String, Object> entities = (Map<String, Object>) requestMap
        .get(IGetConfigDataRequestModel.ENTITIES);
    String moduleId = (String) requestMap.get(IGetConfigDataRequestModel.MODULE_ID);
    String systemLevelId = moduleId != null ? getSystemLevelIdByModuleId(moduleId) : null;
    
    Map<String, Object> attributesResponse = getAttributes(searchColumn, searchText, entities,
        systemLevelId);
    Map<String, Object> tagsResponse = getTags(searchColumn, searchText, entities, systemLevelId);
    Map<String, Object> rolesResponse = getRoles(searchColumn, searchText, entities, systemLevelId);
    Map<String, Object> relationshipsResponse = getRelationships(searchColumn, searchText, entities,
        systemLevelId);
    Map<String, Object> propertyCollectionsResponse = getPropertyCollections(searchColumn,
        searchText, entities, systemLevelId);
    Map<String, Object> attributeVariantContextsResponse = getAttributeVariantsContexts(
        searchColumn, searchText, entities, systemLevelId);
    Map<String, Object> taxonomiesResponse = getTaxonomies(searchColumn, searchText, entities,
        systemLevelId);
    Map<String, Object> tagValuesResponse = getTagValues(searchColumn, searchText, entities);
    Map<String, Object> variantContextsResponse = getVariantContexts(searchColumn, searchText,
        entities, systemLevelId);
    Map<String, Object> tasksResponse = getTasks(searchColumn, searchText, entities, systemLevelId);
    Map<String, Object> dataRulesResponse = getDatarules(searchColumn, searchText, entities,
        systemLevelId);
    Map<String, Object> processesResponse = getProcesses(searchColumn, searchText, entities,
        systemLevelId);
    Map<String, Object> mappingsResponse = getMappings(searchColumn, searchText, entities,
        systemLevelId);
    Map<String, Object> endpointsResponse = getEndpoints(searchColumn, searchText, entities,
        systemLevelId);
    Map<String, Object> systemsResponse = getSystems(searchColumn, searchText, entities,
        systemLevelId);
    Map<String, Object> organizationsResponse = getOrganizations(searchColumn, searchText, entities,
        systemLevelId);
    Map<String, Object> dashboardsResponse = getDashboards(searchColumn, searchText, entities,
        systemLevelId);
    Map<String, Object> kpisResponse = getKPIs(searchColumn, searchText, entities, systemLevelId);
    Map<String, Object> authorizationMappingResponse = getAuthorizationMappings(searchColumn,
        searchText, entities, systemLevelId);
    Map<String, Object> usersResponse = getUsers(searchColumn, searchText, entities, systemLevelId);
    Map<String, Object> rootRelationshipResponse = getRootRelationships(searchColumn, searchText,
        entities, systemLevelId);
    
    Map<String, Object> tabsRequestInfo = (Map<String, Object>) entities
        .get(IGetConfigDataEntityRequestModel.TABS);
    Map<String, Object> tabsResponse = null;
    if (tabsRequestInfo != null) {
      tabsResponse = fetchEntities(tabsRequestInfo, searchColumn, searchText,
          VertexLabelConstants.TAB, systemLevelId);
    }
    
    Map<String, Object> natureRelationshipsRequestInfo = (Map<String, Object>) entities
        .get(IGetConfigDataEntityRequestModel.NATURE_RELATIONSHIPS);
    Map<String, Object> natureRelationshipsResponse = null;
    if (natureRelationshipsRequestInfo != null) {
      natureRelationshipsResponse = fetchEntities(natureRelationshipsRequestInfo, searchColumn,
          searchText, VertexLabelConstants.NATURE_RELATIONSHIP, systemLevelId);
    }
    
    Map<String, Object> contextRequestInfo = (Map<String, Object>) entities
        .get(IGetConfigDataEntityRequestModel.CONTEXTS);
    Map<String, Object> contextResponse = null;
    if (contextRequestInfo != null) {
      contextRequestInfo.put(IS_ATTRIBUTE_CONTEXT, false);
      contextResponse = fetchEntities(contextRequestInfo, searchColumn, searchText,
          VertexLabelConstants.VARIANT_CONTEXT, systemLevelId);
    }
    Map<String, Object> templateRequestInfo = (Map<String, Object>) entities
        .get(IGetConfigDataEntityRequestModel.TEMPLATES);
    Map<String, Object> templateResponse = null;
    if (templateRequestInfo != null) {
      templateResponse = fetchEntities(templateRequestInfo, searchColumn, searchText,
          VertexLabelConstants.TEMPLATE, systemLevelId);
    }
    
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IGetConfigDataResponseModel.ATTRIBUTES, attributesResponse);
    responseMap.put(IGetConfigDataResponseModel.TAGS, tagsResponse);
    responseMap.put(IGetConfigDataResponseModel.ROLES, rolesResponse);
    responseMap.put(IGetConfigDataResponseModel.RELATIONSHIPS, relationshipsResponse);
    responseMap.put(IGetConfigDataResponseModel.PROPERTY_COLLECTIONS, propertyCollectionsResponse);
    responseMap.put(IGetConfigDataResponseModel.ATTRIBUTE_VARIANT_CONTEXTS,
        attributeVariantContextsResponse);
    responseMap.put(IGetConfigDataResponseModel.TAXONOMIES, taxonomiesResponse);
    responseMap.put(IGetConfigDataResponseModel.TAG_VALUES, tagValuesResponse);
    responseMap.put(IGetConfigDataResponseModel.TABS, tabsResponse);
    responseMap.put(IGetConfigDataResponseModel.NATURE_RELATIONSHIPS, natureRelationshipsResponse);
    responseMap.put(IGetConfigDataResponseModel.CONTEXTS, contextResponse);
    responseMap.put(IGetConfigDataResponseModel.TEMPLATES, templateResponse);
    responseMap.put(IGetConfigDataResponseModel.TASKS, tasksResponse);
    responseMap.put(IGetConfigDataResponseModel.DATARULES, dataRulesResponse);
    responseMap.put(IGetConfigDataResponseModel.PROCESSES, processesResponse);
    responseMap.put(IGetConfigDataResponseModel.MAPPINGS, mappingsResponse);
    responseMap.put(IGetConfigDataResponseModel.ENDPOINTS, endpointsResponse);
    responseMap.put(IGetConfigDataResponseModel.SYSTEMS, systemsResponse);
    responseMap.put(IGetConfigDataResponseModel.ORGANIZATIONS, organizationsResponse);
    responseMap.put(IGetConfigDataResponseModel.VARIANT_CONTEXTS, variantContextsResponse);
    responseMap.put(IGetConfigDataResponseModel.DASHBOARD_TABS, dashboardsResponse);
    responseMap.put(IGetConfigDataResponseModel.KPIS, kpisResponse);
    responseMap.put(IGetConfigDataResponseModel.AUTHORIZATION_MAPPING,
        authorizationMappingResponse);
    responseMap.put(IGetConfigDataResponseModel.USERS, usersResponse);
    responseMap.put(IGetConfigDataResponseModel.ROOT_RELATIONSHIPS, rootRelationshipResponse);
    
    return responseMap;
  }
  
  private Map<String, Object> getVariantContexts(String searchColumn, String searchText,
      Map<String, Object> entities, String systemLevelId)
  {
    Map<String, Object> variantContextsRequestInfo = (Map<String, Object>) entities
        .get(IGetConfigDataEntityRequestModel.VARIANT_CONTEXTS);
    Map<String, Object> variantContextsResponse = null;
    if (variantContextsRequestInfo != null) {
      variantContextsResponse = fetchEntities(variantContextsRequestInfo, searchColumn, searchText,
          VertexLabelConstants.VARIANT_CONTEXT, systemLevelId);
    }
    return variantContextsResponse;
  }
  
  private Map<String, Object> getAttributeVariantsContexts(String searchColumn, String searchText,
      Map<String, Object> entities, String systemLevelId)
  {
    Map<String, Object> attributeVariantContextsRequestInfo = (Map<String, Object>) entities
        .get(IGetConfigDataEntityRequestModel.ATTRIBUTE_VARIANT_CONTEXTS);
    Map<String, Object> attributeVariantContextsResponse = null;
    if (attributeVariantContextsRequestInfo != null) {
      attributeVariantContextsRequestInfo.put(IS_ATTRIBUTE_CONTEXT, true);
      attributeVariantContextsResponse = fetchEntities(attributeVariantContextsRequestInfo,
          searchColumn, searchText, CommonConstants.ATTRIBUTE_VARIANT, systemLevelId);
    }
    return attributeVariantContextsResponse;
  }
  
  private Map<String, Object> getSystems(String searchColumn, String searchText,
      Map<String, Object> entities, String systemLevelId)
  {
    Map<String, Object> systemsRequestInfo = (Map<String, Object>) entities
        .get(IGetConfigDataEntityRequestModel.SYSTEMS);
    Map<String, Object> systemsResponse = null;
    if (systemsRequestInfo != null) {
      systemsResponse = fetchEntities(systemsRequestInfo, searchColumn, searchText,
          VertexLabelConstants.SYSTEM, systemLevelId);
    }
    return systemsResponse;
  }
  
  private Map<String, Object> getTasks(String searchColumn, String searchText,
      Map<String, Object> entities, String systemLevelId)
  {
    Map<String, Object> tasksRequestInfo = (Map<String, Object>) entities
        .get(IGetConfigDataEntityRequestModel.TASKS);
    Map<String, Object> tasksResponse = null;
    if (tasksRequestInfo != null) {
      tasksResponse = fetchEntities(tasksRequestInfo, searchColumn, searchText,
          VertexLabelConstants.ENTITY_TYPE_TASK, systemLevelId);
    }
    return tasksResponse;
  }
  
  private Map<String, Object> getDatarules(String searchColumn, String searchText,
      Map<String, Object> entities, String systemLevelId)
  {
    Map<String, Object> dataRulesRequestInfo = (Map<String, Object>) entities
        .get(IGetConfigDataEntityRequestModel.DATARULES);
    Map<String, Object> dataRulesResponse = null;
    if (dataRulesRequestInfo != null) {
      dataRulesResponse = fetchEntities(dataRulesRequestInfo, searchColumn, searchText,
          VertexLabelConstants.DATA_RULE, systemLevelId);
    }
    return dataRulesResponse;
  }
  
  private Map<String, Object> getProcesses(String searchColumn, String searchText,
      Map<String, Object> entities, String systemLevelId)
  {
    Map<String, Object> processesRequestInfo = (Map<String, Object>) entities
        .get(IGetConfigDataEntityRequestModel.PROCESSES);
    Map<String, Object> processesResponse = null;
    if (processesRequestInfo != null) {
      processesResponse = fetchEntities(processesRequestInfo, searchColumn, searchText,
          VertexLabelConstants.PROCESS_EVENT, systemLevelId);
    }
    return processesResponse;
  }
  
  private Map<String, Object> getMappings(String searchColumn, String searchText,
      Map<String, Object> entities, String systemLevelId)
  {
    Map<String, Object> mappingsRequestInfo = (Map<String, Object>) entities
        .get(IGetConfigDataEntityRequestModel.MAPPINGS);
    Map<String, Object> mappingsResponse = null;
    if (mappingsRequestInfo != null) {
      mappingsResponse = fetchEntities(mappingsRequestInfo, searchColumn, searchText,
          VertexLabelConstants.PROPERTY_MAPPING, systemLevelId);
    }
    return mappingsResponse;
  }
  
  private Map<String, Object> getEndpoints(String searchColumn, String searchText,
      Map<String, Object> entities, String systemLevelId)
  {
    Map<String, Object> endpointsRequestInfo = (Map<String, Object>) entities
        .get(IGetConfigDataEntityRequestModel.ENDPOINTS);
    Map<String, Object> endpointsResponse = null;
    if (endpointsRequestInfo != null) {
      endpointsResponse = fetchEntities(endpointsRequestInfo, searchColumn, searchText,
          VertexLabelConstants.ENDPOINT, systemLevelId);
    }
    return endpointsResponse;
  }
  
  private Map<String, Object> getOrganizations(String searchColumn, String searchText,
      Map<String, Object> entities, String systemLevelId)
  {
    Map<String, Object> organizationsRequestInfo = (Map<String, Object>) entities
        .get(IGetConfigDataEntityRequestModel.ORGANIZATIONS);
    Map<String, Object> organizationsResponse = null;
    if (organizationsRequestInfo != null) {
      organizationsResponse = fetchEntities(organizationsRequestInfo, searchColumn, searchText,
          VertexLabelConstants.ORGANIZATION, systemLevelId);
    }
    return organizationsResponse;
  }
  
  private Map<String, Object> getTagValues(String searchColumn, String searchText,
      Map<String, Object> entities) throws Exception
  {
    Map<String, Object> tagValuesRequestInfo = (Map<String, Object>) entities
        .get(IGetConfigDataEntityRequestModel.TAG_VALUES);
    Map<String, Object> contextualTagValuesRequestInfo  =(Map<String, Object>) entities
    	    .get(IGetConfigDataEntityRequestModel.CONTEXT_TAG_VALUES);
    Map<String, Object> tagValuesResponse = null;
    if (tagValuesRequestInfo != null) {
      tagValuesResponse = fetchTagValues(tagValuesRequestInfo, searchColumn, searchText,
          VertexLabelConstants.ENTITY_TAG);
    }
    else if (contextualTagValuesRequestInfo != null){
        /*
         * Hence it means we are fetching contextual tag values for table view
         */
        tagValuesResponse =  getcontextTagValues(searchColumn, searchText, entities);
      }
    return tagValuesResponse;
  }
  
  private Map<String, Object> getcontextTagValues(String searchColumn, String searchText,
	      Map<String, Object> entities) throws Exception
	  {
	    Map<String, Object> tagValuesRequestInfo = (Map<String, Object>) entities.get(IGetConfigDataEntityRequestModel.CONTEXT_TAG_VALUES);
	    Long from = Long.valueOf(tagValuesRequestInfo.get(IGetConfigDataEntityPaginationModel.FROM).toString());
	    Long size = Long.valueOf(tagValuesRequestInfo.get(IGetConfigDataEntityPaginationModel.SIZE).toString());
	    String sortBy = (String) tagValuesRequestInfo.get(IGetConfigDataEntityPaginationModel.SORT_BY);
	    String sortOrder = (String) tagValuesRequestInfo.get(IGetConfigDataEntityPaginationModel.SORT_ORDER);
	    
	    Map<String, Object> entitiesResponse = new HashMap<>();
	    List<Map<String, Object>> tagValuesToReturnList = new ArrayList<>();
	    
	    if (tagValuesRequestInfo != null) {
	      StringBuilder searchQuery = EntityUtil.getConditionQuery(EntityUtil.getSearchQuery(searchText, searchColumn));
	      String contextId = (String) tagValuesRequestInfo.get(IGetConfigDataTagValuesPaginationModel.CONTEXT_ID);
	      String tagGroupId = (String) tagValuesRequestInfo.get(IGetConfigDataTagValuesPaginationModel.TAG_GROUP_ID);
	      Vertex contextNode = UtilClass.getVertexById(contextId, VertexLabelConstants.VARIANT_CONTEXT);
	      String rid = contextNode.getId().toString();

	      String query = "select from (select expand(outE('Has_Context_Tag')[tagId = "
	          + EntityUtil.quoteIt(tagGroupId) + "].inV().out('Has_Context_Tag_Value')) from " + rid
	          + ") " + searchQuery + " order by " + EntityUtil.getLanguageConvertedField(sortBy) + " "
	          + sortOrder + " skip " + from + " limit " + size;
	      
	      executeQueryAndPrepareResponse(query, VertexLabelConstants.ENTITY_TAG, tagValuesToReturnList);
	    }
	    
	    entitiesResponse.put(IGetConfigDataEntityResponseModel.FROM, from);
	    entitiesResponse.put(IGetConfigDataEntityResponseModel.SIZE, size);
	    entitiesResponse.put(IGetConfigDataEntityResponseModel.LIST, tagValuesToReturnList);
	    entitiesResponse.put(IGetConfigDataEntityResponseModel.TOTAL_COUNT, tagValuesToReturnList.size());
	    return entitiesResponse;
	  }
  
  private Map<String, Object> getTaxonomies(String searchColumn, String searchText,
      Map<String, Object> entities, String systemLevelId)
  {
    Map<String, Object> taxonomiesRequestInfo = (Map<String, Object>) entities
        .get(IGetConfigDataEntityRequestModel.TAXONOMIES);
    Map<String, Object> taxonomiesResponse = null;
    if (taxonomiesRequestInfo != null) {
      taxonomiesResponse = fetchEntities(taxonomiesRequestInfo, searchColumn, searchText,
          VertexLabelConstants.ROOT_KLASS_TAXONOMY, systemLevelId);
    }
    return taxonomiesResponse;
  }
  
  private Map<String, Object> getPropertyCollections(String searchColumn, String searchText,
      Map<String, Object> entities, String systemLevelId)
  {
    Map<String, Object> propertyCollectionsRequestInfo = (Map<String, Object>) entities
        .get(IGetConfigDataEntityRequestModel.PROPERTY_COLLECTIONS);
    Map<String, Object> propertyCollectionsResponse = null;
    if (propertyCollectionsRequestInfo != null) {
      propertyCollectionsResponse = fetchEntities(propertyCollectionsRequestInfo, searchColumn,
          searchText, VertexLabelConstants.PROPERTY_COLLECTION, systemLevelId);
    }
    return propertyCollectionsResponse;
  }
  
  private Map<String, Object> getRelationships(String searchColumn, String searchText,
      Map<String, Object> entities, String systemLevelId)
  {
    Map<String, Object> relationshipsRequestInfo = (Map<String, Object>) entities
        .get(IGetConfigDataEntityRequestModel.RELATIONSHIPS);
    Map<String, Object> relationshipsResponse = null;
    if (relationshipsRequestInfo != null) {
      relationshipsResponse = fetchEntities(relationshipsRequestInfo, searchColumn, searchText,
          VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP, systemLevelId);
    }
    return relationshipsResponse;
  }
  
  private Map<String, Object> getRoles(String searchColumn, String searchText,
      Map<String, Object> entities, String systemLevelId)
  {
    Map<String, Object> rolesRequestInfo = (Map<String, Object>) entities
        .get(IGetConfigDataEntityRequestModel.ROLES);
    Map<String, Object> rolesResponse = null;
    if (rolesRequestInfo != null) {
      rolesResponse = fetchEntities(rolesRequestInfo, searchColumn, searchText,
          VertexLabelConstants.ENTITY_TYPE_ROLE, systemLevelId);
    }
    return rolesResponse;
  }
  
  private Map<String, Object> getTags(String searchColumn, String searchText,
      Map<String, Object> entities, String systemLevelId)
  {
    Map<String, Object> tagsRequestInfo = (Map<String, Object>) entities
        .get(IGetConfigDataEntityRequestModel.TAGS);
    Map<String, Object> tagsResponse = null;
    if (tagsRequestInfo != null) {
      tagsResponse = fetchEntities(tagsRequestInfo, searchColumn, searchText,
          VertexLabelConstants.ENTITY_TAG, systemLevelId);
    }
    return tagsResponse;
  }
  
  private Map<String, Object> getAttributes(String searchColumn, String searchText,
      Map<String, Object> entities, String systemLevelId)
  {
    Map<String, Object> attributesRequestInfo = (Map<String, Object>) entities
        .get(IGetConfigDataEntityRequestModel.ATTRIBUTES);
    Map<String, Object> attributesResponse = null;
    if (attributesRequestInfo != null) {
      attributesResponse = fetchEntities(attributesRequestInfo, searchColumn, searchText,
          VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE, systemLevelId);
    }
    return attributesResponse;
  }
  
  private String getSystemLevelIdByModuleId(String moduleId)
  {
    switch (moduleId) {
      case Constants.ALL_MODULE:
        return "";
      case Constants.PIM_MODULE:
        return CommonConstants.ARTICLE_INSTANCE_MODULE_ENTITY;
      
      case Constants.MAM_MODULE:
        return CommonConstants.ASSET_INSTANCE_MODULE_ENTITY;
      
      case Constants.TARGET_MODULE:
        return CommonConstants.MARKET_INSTANCE_MODULE_ENTITY;
      
      case Constants.TEXT_ASSET_MODULE:
        return CommonConstants.TEXT_ASSET_INSTANCE_MODULE_ENTITY;

      case Constants.SUPPLIER_MODULE:
        return CommonConstants.SUPPLIER_INSTANCE_MODULE_ENTITY;
      
      case Constants.FILES_MODUlE:
        return CommonConstants.FILE_INSTANCE_MODULE_ENTITY;
      
    }
    return null;
  }
  
  private List<String> getFieldsToFetch(String entityLabel)
  {
    switch (entityLabel) {
      case VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE:
        return FIELDS_TO_FETCH_FOR_ATTRIBUTE;
      case VertexLabelConstants.ENTITY_TAG:
        return FIELDS_TO_FETCH_FOR_TAG;
      case VertexLabelConstants.PROPERTY_COLLECTION:
        return FIELDS_TO_FETCH_FOR_PCS;
      case VertexLabelConstants.GOVERNANCE_RULE_KPI:
        return FIELDS_TO_FETCH_FOR_KPI;
      case VertexLabelConstants.ENTITY_TYPE_USER:
        return FIELDS_TO_FETCH_FOR_USER;
      case VertexLabelConstants.ROOT_KLASS_TAXONOMY:
        return FIELDS_TO_FETCH_FOR_TAXONOMY;
      case VertexLabelConstants.PROCESS_EVENT:
        return FIELDS_TO_FETCH_FOR_PROCESS;
      default:
        return FIELDS_TO_FETCH;
    }
  }
  
  private Map<String, Object> fetchTagValues(Map<String, Object> entityRequestInfo,
      String searchColumn, String searchText, String entityLabel) throws Exception
  {
    Long from = Long.valueOf(entityRequestInfo.get(IGetConfigDataEntityPaginationModel.FROM)
        .toString());
    Long size = Long.valueOf(entityRequestInfo.get(IGetConfigDataEntityPaginationModel.SIZE)
        .toString());
    List<String> klassIds = (List<String>) entityRequestInfo
        .get(IGetConfigDataTagValuesPaginationModel.KLASS_TYPES);
    List<String> selectedTaxonomyIds = (List<String>) entityRequestInfo
        .get(IGetConfigDataTagValuesPaginationModel.SELECTED_TAXONOMY_IDS);
    String taxonomyId = (String) entityRequestInfo
        .get(IGetConfigDataTagValuesPaginationModel.TAXONOMY_ID);
    
    String tagGroupId = (String) entityRequestInfo
        .get(IGetConfigDataTagValuesPaginationModel.TAG_GROUP_ID);
    Vertex tagGroupNode = UtilClass.getVertexById(tagGroupId, VertexLabelConstants.ENTITY_TAG);
    Map<String, Object> tagMap = UtilClass.getMapFromVertex(Arrays.asList(ITag.TAG_VALUES_SEQUENCE),
        tagGroupNode);
    List<String> tagValuesSequence = (List<String>) tagMap.get(ITag.TAG_VALUES_SEQUENCE);
    final List<String> tagValuesSequenceCopy = tagValuesSequence;
    String klassPropertyNodeId = (String) entityRequestInfo
        .get(IGetConfigDataTagValuesPaginationModel.ELEMENT_ID);
    List<Map<String, Object>> tagValuesToReturnList = new ArrayList<>();
    
    // Get Default tag values depending on Limited tag values
    if (klassPropertyNodeId != null && !klassPropertyNodeId.isEmpty()
        && (searchText == null || searchText.isEmpty())) {
      Boolean isLimitedTagValuesSelected = isLimitedTagValuesSelected(klassPropertyNodeId,
          entityLabel);
      Boolean isFound = getDefaultTagValueDataDependingOnSelectedTagValues(klassPropertyNodeId,
          entityLabel, tagValuesToReturnList, from, size, searchText, false);
      if (!isFound && !isLimitedTagValuesSelected) {
        getTagValueDataWithoutElementId(searchColumn, searchText, entityLabel, from, size,
            tagGroupId, tagValuesToReturnList);
      }
    }
    else if (klassPropertyNodeId != null && !klassPropertyNodeId.isEmpty() && searchText != null
        && !searchText.isEmpty()) {
      Boolean isLimitedTagValuesSelected = isLimitedTagValuesSelected(klassPropertyNodeId,
          entityLabel);
      if (isLimitedTagValuesSelected) {
        getDefaultTagValueDataDependingOnSelectedTagValues(klassPropertyNodeId, entityLabel,
            tagValuesToReturnList, from, size, searchText, true);
        tagValuesToReturnList = searchOnTagValuesList(tagValuesToReturnList, searchText, from,
            size);
      }
      else {
        getTagValueDataWithoutElementId(searchColumn, searchText, entityLabel, from, size,
            tagGroupId, tagValuesToReturnList);
      }
    }
    else if (klassIds != null && selectedTaxonomyIds != null) {
      klassIds.addAll(selectedTaxonomyIds);
      List<String> allowedTagValueIds = KlassTagUtil.getAllowedTagValueIds(tagGroupId, klassIds,
          searchColumn, searchText);
      
      // pagination
      tagValuesSequence.retainAll(allowedTagValueIds);
      Long to = from + size;
      if (from >= tagValuesSequence.size()) {
        tagValuesToReturnList = new ArrayList<>();
      }
      else {
        
        if (to > tagValuesSequence.size()) {
          to = tagValuesSequence.size() + 0l;
        }
        List<String> paginatedTagValueSequence = tagValuesSequence.subList(from.intValue(),
            to.intValue());
        
        // in query
        getTagValuesList(entityLabel, paginatedTagValueSequence, tagValuesToReturnList);
        
        // sort
        tagValuesToReturnList = tagValuesToReturnList.stream()
            .sorted((obj1,
                obj2) -> ((Integer) tagValuesSequenceCopy
                    .indexOf(obj1.get(CommonConstants.ID_PROPERTY))).compareTo(
                        tagValuesSequenceCopy.indexOf(obj2.get(CommonConstants.ID_PROPERTY))))
            .collect(Collectors.toList());
      }
    }
    else if (taxonomyId != null && !taxonomyId.isEmpty()) {
      // PXPFDEV-21215 : Deprecated Taxonomy Hierarchies
    }
    else {
      getTagValueDataWithoutElementId(searchColumn, searchText, entityLabel, from, size, tagGroupId,
          tagValuesToReturnList);
    }
    
    Map<String, Object> entitiesResponse = new HashMap<>();
    entitiesResponse.put(IGetConfigDataEntityResponseModel.FROM, from);
    entitiesResponse.put(IGetConfigDataEntityResponseModel.SIZE, size);
    entitiesResponse.put(IGetConfigDataEntityResponseModel.LIST, tagValuesToReturnList);
    entitiesResponse.put(IGetConfigDataEntityResponseModel.TOTAL_COUNT,
        tagValuesToReturnList.size());
    
    return entitiesResponse;
  }
  
  private void getTagValuesList(String entityLabel, List<String> tagValuesSequence,
      List<Map<String, Object>> tagValuesToReturnList)
  {
    String query = "select from " + entityLabel + " where code in ['"
        + String.join("','", tagValuesSequence) + "']";
    
    executeQueryAndPrepareResponse(query, VertexLabelConstants.ENTITY_TAG, tagValuesToReturnList);
  }
  
  private void getPaginatedResultList(Long from, Long size,
      List<Map<String, Object>> tagValuesToReturnList)
  {
    Integer selectedTagValuesCount = tagValuesToReturnList.size();
    
    Long to = from + size;
    if (to > selectedTagValuesCount) {
      to = selectedTagValuesCount + 0l;
    }
    List<Map<String, Object>> clonedEntitiesList = new ArrayList<>(tagValuesToReturnList);
    tagValuesToReturnList.clear();
    tagValuesToReturnList.addAll(clonedEntitiesList.subList(from.intValue(), to.intValue()));
  }

  private List<Map<String, Object>> searchOnTagValuesList(
      List<Map<String, Object>> tagValuesToReturnList, String searchText, Long from, Long size)
      throws Exception
  {
    List<Map<String, Object>> tagValuesList = new ArrayList<>();
    Long fromCounter = 0l, sizeCounter = 0l;
    for (Map<String, Object> tagValue : tagValuesToReturnList) {
      String tagValueId = (String) tagValue.get(CommonConstants.ID_PROPERTY);
      Vertex tagValueNode = UtilClass.getVertexById(tagValueId, VertexLabelConstants.ENTITY_TAG);
      String label = tagValueNode
          .getProperty(EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY));
      if (label != null && label.toLowerCase()
          .contains(searchText.toLowerCase())) {
        if (fromCounter >= from) {
          if (sizeCounter < size) {
            tagValuesList.add(tagValue);
            sizeCounter++;
          }
        }
        fromCounter++;
      }
    }
    return tagValuesList;
  }
  
  private Boolean getDefaultTagValueDataDependingOnSelectedTagValues(String elementId,
      String entityLabel, List<Map<String, Object>> entitiesList, Long from, Long size,
      String searchText, boolean isSearch) throws Exception
  {
    String search = "";
    if (!isSearch) {
      search = " skip " + from + " limit " + size;
    }
    
    String query = "select from (select expand(out('"
        + RelationshipLabelConstants.HAS_KLASS_TAG_VALUE + "')) from "
        + VertexLabelConstants.KLASS_TAG + " where code = '" + elementId + "') "
        + " order by label ASC " + search;
    
    return executeQueryAndPrepareResponse(query, entityLabel, entitiesList);
  }
  
  private Boolean isLimitedTagValuesSelected(String elementId, String entityLabel) throws Exception
  {
    Vertex KPNode = UtilClass.getVertexById(elementId, VertexLabelConstants.KLASS_TAG);
    Iterator<Edge> limitedTagValuesEdges = KPNode
        .getEdges(Direction.OUT, RelationshipLabelConstants.HAS_KLASS_TAG_VALUE)
        .iterator();
    
    return limitedTagValuesEdges.hasNext();
  }
  
  private Boolean executeQueryAndPrepareResponse(String query, String entityLabel,
      List<Map<String, Object>> entitiesList)
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> searchResults = graph.command(new OCommandSQL(query))
        .execute();
    Boolean found = false;
    for (Vertex searchResult : searchResults) {
      Map<String, Object> entityMap = UtilClass.getMapFromVertex(getFieldsToFetch(entityLabel),
          searchResult);
      entitiesList.add(entityMap);
      found = true;
    }
    
    return found;
  }
  
  private void getTagValueDataWithoutElementId(String searchColumn, String searchText,
      String entityLabel, Long from, Long size, String tagGroupId,
      List<Map<String, Object>> entitiesList) throws Exception, TagNotFoundException
  {
    Vertex tagGroupNode = null;
    try {
      tagGroupNode = UtilClass.getVertexById(tagGroupId, VertexLabelConstants.ENTITY_TAG);
    }
    catch (NotFoundException e) {
      throw new TagNotFoundException();
    }
    
    List<String> tagValuesSequence = tagGroupNode.getProperty(ITag.TAG_VALUES_SEQUENCE);
    
    if (tagValuesSequence == null) {
      tagValuesSequence = new ArrayList<>();
    }
    
    Integer totalTagValuesCount = tagValuesSequence.size();
    
    if (from < totalTagValuesCount) {
      /*
      Long to = from + size;
      if (to > tagValuesCount) {
        to = tagValuesCount + 0l;
      }
      
      tagValuesSequence = tagValuesSequence.subList(from.intValue(), to.intValue());
      */
      
      if (!tagValuesSequence.isEmpty()) {
        String searchQuery = "";
        if (!searchColumn.isEmpty() && !searchText.isEmpty()) {
          if (IStandardTranslationModel.TRNASLATION_FIELDS.contains(searchColumn)) {
            searchColumn = EntityUtil.getLanguageConvertedField(searchColumn);
          }
          searchQuery = " AND " + searchColumn + " like '%" + searchText + "%'";
        }
        String query = "select from " + entityLabel + " where code in ['"
            + String.join("','", tagValuesSequence) + "']" + searchQuery;
        
        executeQueryAndPrepareResponse(query, entityLabel, entitiesList);
      }
    }
    
    final List<String> tagValuesSequenceCopy = tagValuesSequence;
    
    if (!entitiesList.isEmpty()) {
      entitiesList.sort((Map<String, Object> obj1,
          Map<String, Object> obj2) -> ((Integer) tagValuesSequenceCopy
              .indexOf(obj1.get(CommonConstants.ID_PROPERTY)))
                  .compareTo(tagValuesSequenceCopy.indexOf(obj2.get(CommonConstants.ID_PROPERTY))));
      
      getPaginatedResultList(from, size, entitiesList);
    }
  }
  
  private Map<String, Object> fetchEntities(Map<String, Object> entityRequestInfo,
      String searchColumn, String searchText, String entityLabel, String systemLevelId)
  {
    Long from = Long.valueOf(entityRequestInfo.get(IGetConfigDataEntityPaginationModel.FROM)
        .toString());
    Long size = Long.valueOf(entityRequestInfo.get(IGetConfigDataEntityPaginationModel.SIZE)
        .toString());
    String sortBy = (String) entityRequestInfo.get(IGetConfigDataEntityPaginationModel.SORT_BY);
    String sortOrder = (String) entityRequestInfo
        .get(IGetConfigDataEntityPaginationModel.SORT_ORDER);
    List<String> types = (List<String>) entityRequestInfo
        .get(IGetConfigDataEntityPaginationModel.TYPES);
    Map<String, Object> properties = (Map<String, Object>) entityRequestInfo
        .get(IGetConfigDataEntityPaginationModel.PROPERTIES);
    List<String> conditions = getConditions(entityLabel, entityRequestInfo);
    Long totalCount = getTotalCount(searchColumn, searchText, entityLabel, conditions,
        systemLevelId, types);
    
    Iterable<Vertex> searchResults = getVerticesAccordingToRequest(searchColumn, searchText, from,
        size, sortBy, sortOrder, properties, entityLabel, conditions, systemLevelId, types);
    List<Map<String, Object>> entitiesList = new ArrayList<>();
    for (Vertex searchResult : searchResults) {
      Map<String, Object> entityMap = UtilClass.getMapFromVertex(getFieldsToFetch(entityLabel),
          searchResult);
      if(entityMap.containsKey(CommonConstants.BASE_TYPE)) {
        entityMap.put(CommonConstants.TYPE_PROPERTY, entityMap.get(CommonConstants.BASE_TYPE));
        entityMap.remove(CommonConstants.BASE_TYPE);
      }
      entitiesList.add(entityMap);
    }
    
    Map<String, Object> entitiesResponse = new HashMap<>();
    entitiesResponse.put(IGetConfigDataEntityResponseModel.FROM, from);
    entitiesResponse.put(IGetConfigDataEntityResponseModel.SIZE, size);
    entitiesResponse.put(IGetConfigDataEntityResponseModel.TOTAL_COUNT, totalCount);
    entitiesResponse.put(IGetConfigDataEntityResponseModel.LIST, entitiesList);
    
    return entitiesResponse;
  }
  
  private Iterable<Vertex> getVerticesAccordingToRequest(String searchColumn, String searchText,
      Long from, Long size, String sortBy, String sortOrder, Map<String, Object> properties,
      String entityLabel, List<String> conditions, String systemLevelId, List<String> types)
  {
    String query = generateGetQuery(searchColumn, searchText, from, size, sortBy, sortOrder,
        properties, entityLabel, conditions, systemLevelId, types);
    
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> searchResults = graph.command(new OCommandSQL(query))
        .execute();
    return searchResults;
  }
  
  private Long getTotalCount(String searchColumn, String searchText, String entityLabel,
      List<String> conditions, String systemLevelId, List<String> types)
  {
    String query = "select count(*) from " + entityLabel + " where outE('Child_Of').size() = 0";
    
    if(entityLabel.equals(VertexLabelConstants.ENTITY_TAG)) {
      query = "select count(*) from "+ VertexLabelConstants.ENTITY_TAG + " where " + ITag.IS_ROOT + " = true";
    }
    
    if (entityLabel.equals(CommonConstants.ATTRIBUTE_VARIANT)) {
      query = "select count(*) from " + VertexLabelConstants.VARIANT_CONTEXT
          + " where outE('Child_Of').size() = 0";
      query += " AND " + IVariantContext.TYPE + " = \"" + CommonConstants.ATTRIBUTE_VARIANT_CONTEXT
          + "\"";
    }
    if (entityLabel.equals(VertexLabelConstants.ROOT_KLASS_TAXONOMY)) {
      query += " AND " + IMasterTaxonomyModel.TAXONOMY_TYPE + " = \""
          + CommonConstants.MINOR_TAXONOMY + "\"";
    }
    
    if (entityLabel.equals(VertexLabelConstants.VARIANT_CONTEXT)) {
      StringBuilder typesQuery = UtilClass.getTypeQuery(types, IVariantContext.TYPE);
      query += typesQuery;
    }
    if (entityLabel.equals(VertexLabelConstants.ENDPOINT)) {
      StringBuilder typesQuery = UtilClass.getTypeQuery(types, IEndpoint.ENDPOINT_TYPE);
      query += typesQuery;
    }
    if (entityLabel.equals(VertexLabelConstants.PROCESS_EVENT)) {
      StringBuilder typesQuery = UtilClass.getTypeQuery(types, IProcessEvent.PROCESS_TYPE);
      query += typesQuery;
    }
    
    if (!UtilClass.isStringNullOrEmpty(searchColumn)
        && !UtilClass.isStringNullOrEmpty(searchText)) {
      searchColumn = EntityUtil.getLanguageConvertedField(searchColumn);
      query += " AND " + searchColumn + " like '%" + searchText + "%'";
    }
    
    if ((entityLabel.equals(VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE)) && systemLevelId != null
        && !systemLevelId.equals("")
        && !systemLevelId.equals(CommonConstants.FILE_INSTANCE_MODULE_ENTITY)) {
      StringBuilder typesQuery = UtilClass.getTypeQuery(types, IAttribute.TYPE);
      query += " AND ( '" + systemLevelId + "' in availability Or availability.size() = 0 )"
          + typesQuery;
    }
    if ((entityLabel.equals(VertexLabelConstants.ENTITY_TAG)) && systemLevelId != null
        && !systemLevelId.equals("")
        && !systemLevelId.equals(CommonConstants.FILE_INSTANCE_MODULE_ENTITY)) {
      StringBuilder typesQuery = UtilClass.getTypeQuery(types, ITag.TAG_TYPE);
      query += " AND ( '" + systemLevelId + "' in availability Or availability.size() = 0 )"
          + typesQuery;
    }
    
    if (entityLabel.equals(VertexLabelConstants.ENTITY_TAG)) {
      StringBuilder typesQuery = UtilClass.getTypeQuery(types, ITag.TAG_TYPE);
      query += " and " + ITag.TYPE + " is not null " + typesQuery;
    }
    
    if (!conditions.isEmpty()) {
      query += " AND " + String.join(" AND ", conditions.toArray(new String[conditions.size()]));
    }
    
    return EntityUtil.executeCountQueryToGetTotalCount(query);
  }
  
  private String generateGetQuery(String searchColumn, String searchText, Long from, Long size,
      String sortBy, String sortOrder, Map<String, Object> properties, String entityLabel,
      List<String> conditions, String systemLevelId, List<String> types)
  {
    String query = "select from " + entityLabel + " where outE('Child_Of').size() = 0";
    
    if(entityLabel.equals(VertexLabelConstants.ENTITY_TAG)) {
      query = "select from " + VertexLabelConstants.ENTITY_TAG + " where " + ITag.IS_ROOT + " = true"; 
    }
    
    if (entityLabel.equals(CommonConstants.ATTRIBUTE_VARIANT)) {
      query = "select from " + VertexLabelConstants.VARIANT_CONTEXT
          + " where outE('Child_Of').size() = 0";
      query += " and " + IVariantContext.TYPE + " = \"" + CommonConstants.ATTRIBUTE_VARIANT_CONTEXT
          + "\"";
    }
    
    if (entityLabel.equals(VertexLabelConstants.ROOT_KLASS_TAXONOMY)) {
      
      properties = properties == null ? new HashMap<>() : properties;
      List<String> taxonomyTypes = (List<String>) properties.get("taxonomyTypes");
      
      if (taxonomyTypes == null || taxonomyTypes.isEmpty()) {
        query = "SELECT FROM (TRAVERSE in('" + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF
            + "')" + " FROM " + VertexLabelConstants.ROOT_KLASS_TAXONOMY + ") WHERE ("
            + IMasterTaxonomy.IS_TAXONOMY + " IS NULL OR " + IMasterTaxonomy.IS_TAXONOMY
            + " = true ) AND ";
      }
      else if (taxonomyTypes != null && taxonomyTypes.contains(Constants.MASTER_TAXONOMY)) {
        query = "SELECT FROM (TRAVERSE in('" + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF
            + "')" + " FROM " + VertexLabelConstants.ATTRIBUTION_TAXONOMY + ") WHERE ("
            + IMasterTaxonomy.IS_TAXONOMY + " IS NULL OR " + IMasterTaxonomy.IS_TAXONOMY
            + " = true ) AND ";
      }
      
      Boolean isRoot = (Boolean) properties.get("isRoot");
      if (isRoot == null) {
        isRoot = false;
      }
      if ((isRoot != null && isRoot) || (types != null && !types.isEmpty())) {
        query += "outE('" + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF
            + "').size() = 0  AND ";
      }
      
      if (types != null && types.contains(CommonConstants.MAJOR_TAXONOMY)) {
        
        query += ITaxonomy.TAXONOMY_TYPE + " = \"" + CommonConstants.MAJOR_TAXONOMY + "\" ";
        
        if (!isRoot) {
          List<ORecordId> rootTaxonomyIds = getRootTaxonomyIds(query);
          
          if (!rootTaxonomyIds.isEmpty()) {
            query = "SELECT FROM (TRAVERSE IN('"
                + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF + "') FROM "
                + rootTaxonomyIds + ") WHERE ";
          }
        }
      }
      
      if (types != null && types.contains(CommonConstants.MINOR_TAXONOMY)) {
        query += ITaxonomy.TAXONOMY_TYPE + " = \"" + CommonConstants.MINOR_TAXONOMY + "\" ";
        
        if (!isRoot) {
          List<ORecordId> rootTaxonomyIds = getRootTaxonomyIds(query);
          
          if (!rootTaxonomyIds.isEmpty()) {
            query = "SELECT FROM (TRAVERSE IN('"
                + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF + "') FROM "
                + rootTaxonomyIds + ") WHERE ";
          }
        }
      }
    }
    
    if (entityLabel.equals(VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE)) {
      StringBuilder typesQuery = UtilClass.getTypeQuery(types, IAttribute.TYPE);
      query += typesQuery;
    }
    
    if (entityLabel.equals(VertexLabelConstants.ENTITY_TAG)) {
      StringBuilder typesQuery = UtilClass.getTypeQuery(types, ITag.TAG_TYPE);
      query += " and " + ITag.TYPE + " is not null " + typesQuery;
    }
    if (entityLabel.equals(VertexLabelConstants.VARIANT_CONTEXT)) {
      StringBuilder typesQuery = UtilClass.getTypeQuery(types, IVariantContext.TYPE);
      query += typesQuery;
    }
    if (entityLabel.equals(VertexLabelConstants.ENDPOINT)) {
      StringBuilder typesQuery = UtilClass.getTypeQuery(types, IEndpoint.ENDPOINT_TYPE);
      query += typesQuery;
    }
    if (entityLabel.equals(VertexLabelConstants.PROCESS_EVENT)) {
      query = "select from " + entityLabel;
      query += " where " + CommonConstants.CODE_PROPERTY + " not in "
          + EntityUtil.quoteIt(SystemLevelIds.WORKFLOWS_TO_EXCLUDE_FROM_CONFIG_SCREEN);
      StringBuilder typesQuery = UtilClass.getTypeQuery(types, IProcessEvent.PROCESS_TYPE);
      query += typesQuery;
    }
    if (entityLabel.equals(VertexLabelConstants.PROPERTY_MAPPING)) {
      StringBuilder typesQuery = UtilClass.getTypeQuery(types, IMapping.MAPPING_TYPE);
      query += typesQuery;
    }
    
    if (!conditions.isEmpty()) {
      query += " AND " + String.join(" AND ", conditions.toArray(new String[conditions.size()]));
    }
    
    if ((entityLabel.equals(VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE)
        || (entityLabel.equals(VertexLabelConstants.ENTITY_TAG))) && systemLevelId != null
        && !systemLevelId.equals("")
        && !systemLevelId.equals(CommonConstants.FILE_INSTANCE_MODULE_ENTITY)) {
      query += " AND ( '" + systemLevelId + "' in availability Or availability.size() = 0 )";
    }
    
    String queryParameters = getQueryParametersInString(searchColumn, searchText, from, size,
        sortBy, sortOrder);
    if (!UtilClass.isStringNullOrEmpty(searchColumn)
        && !UtilClass.isStringNullOrEmpty(searchText)) {
      if (query.endsWith(" AND ")) {
        int lastIndexOf = query.lastIndexOf(" AND ");
        query = query.substring(0, lastIndexOf);
      }
      
      if (query.endsWith(" WHERE ")) {
        queryParameters = queryParameters.replaceFirst(" AND ", "");
      }
    }
    else {
      
      if (query.endsWith(" AND ")) {
        int lastIndexOf = query.lastIndexOf(" AND ");
        query = query.substring(0, lastIndexOf);
      }
      
      if (query.endsWith(" WHERE ")) {
        int lastIndexOf = query.lastIndexOf(" WHERE ");
        query = query.substring(0, lastIndexOf);
      }
    }
    
    query += queryParameters;
    return query;
  }
  
  private List<ORecordId> getRootTaxonomyIds(String query)
  {
    Iterable<Vertex> searchResults = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    List<ORecordId> rootTaxonomyIds = new ArrayList<>();
    for (Vertex taxonomy : searchResults) {
      rootTaxonomyIds.add((ORecordId) taxonomy.getId());
    }
    return rootTaxonomyIds;
  }
  
  private String getQueryParametersInString(String searchColumn, String searchText, Long from,
      Long size, String sortBy, String sortOrder)
  {
    String queryParameters = "";
    
    if (!UtilClass.isStringNullOrEmpty(searchColumn)
        && !UtilClass.isStringNullOrEmpty(searchText)) {
      searchColumn = EntityUtil.getLanguageConvertedField(searchColumn);
      queryParameters += " AND " + searchColumn + " like '%" + searchText + "%'";
    }
    
    if (!UtilClass.isStringNullOrEmpty(sortBy)) {
      sortBy = EntityUtil.getLanguageConvertedField(sortBy);
      if (UtilClass.isStringNullOrEmpty(sortOrder)) {
        sortOrder = "asc";
      }
      queryParameters += " order by " + sortBy + " " + sortOrder;
    }
    
    if (from != null && size != null) {
      queryParameters += " skip " + from + " limit " + size;
    }
    
    return queryParameters;
  }
  
  private List<String> getConditions(String entityLabel, Map<String, Object> entityRequestInfo)
  {
    List<String> conditions = new ArrayList<>();
    List<String> typesToExclude = new ArrayList<>();
    Boolean isDisabled;
    
    switch (entityLabel) {
      case VertexLabelConstants.PROPERTY_COLLECTION:
        fillConditionsForPropertyCollections(conditions, entityRequestInfo);
        break;
      case VertexLabelConstants.VARIANT_CONTEXT:
        fillConditionsForVariantContext(conditions, entityRequestInfo);
        break;
      case VertexLabelConstants.TAB:
        typesToExclude = (List<String>) entityRequestInfo
            .get(IGetConfigDataEntityPaginationModel.TYPES_TO_EXCLUDE);
        if (typesToExclude == null) {
          typesToExclude = new ArrayList<>();
        }
        typesToExclude.addAll(IStandardConfig.StandardTab.DefaultRuntimeTabs);
        fillConditionForTab(conditions, entityRequestInfo, typesToExclude);
        break;
      case VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE:
        typesToExclude = (List<String>) entityRequestInfo
            .get(IGetConfigDataEntityPaginationModel.TYPES_TO_EXCLUDE);
        if (typesToExclude != null && !typesToExclude.isEmpty()) {
          fillConditionForAttributeTypeToExclude(conditions, typesToExclude);
        }
        List<String> idsToExclude = (List<String>) entityRequestInfo.get(IGetConfigDataEntityPaginationModel.IDS_TO_EXCLUDE);
        if (!idsToExclude.isEmpty()) {
          fillConditionForAttributeIdsToExclude(conditions, idsToExclude);
        }
        
        UtilClass.fillConditionForLanguageAttributeQuery(conditions, entityRequestInfo);
        isDisabled = (Boolean) entityRequestInfo
            .get(IGetConfigDataEntityPaginationModel.IS_DISABLED);
        if (isDisabled != null) {
          fillConditionForDisabledElement(conditions, isDisabled);
        }
        break;
      
      case VertexLabelConstants.ENTITY_TAG:
        typesToExclude = (List<String>) entityRequestInfo
            .get(IGetConfigDataEntityPaginationModel.TYPES_TO_EXCLUDE);
        if (typesToExclude != null && !typesToExclude.isEmpty()) {
          fillConditionForTagTypeToExclude(conditions, typesToExclude);
        }
        isDisabled = (Boolean) entityRequestInfo
            .get(IGetConfigDataEntityPaginationModel.IS_DISABLED);
        if (isDisabled != null) {
          fillConditionForDisabledElement(conditions, isDisabled);
        }
        break;
      
      case VertexLabelConstants.ROOT_RELATIONSHIP:
        typesToExclude = (List<String>) entityRequestInfo
            .get(IGetConfigDataEntityPaginationModel.TYPES_TO_EXCLUDE);
        if (typesToExclude == null) {
          typesToExclude = new ArrayList<>();
        }
        fillConditionForRootRelationshipTypesToExclude(conditions, typesToExclude);
        break;
      case VertexLabelConstants.PROCESS_EVENT:
        fillConditionForProcessEvent(conditions, entityRequestInfo);
        break;
      case VertexLabelConstants.ENTITY_TYPE_TASK:
        typesToExclude = (List<String>) entityRequestInfo.get(IGetConfigDataEntityPaginationModel.TYPES_TO_EXCLUDE);
        if (typesToExclude != null && !typesToExclude.isEmpty()) {
          fillConditionForTaskToExclude(conditions, typesToExclude);
        }
        break;
      case VertexLabelConstants.ENDPOINT:
        
        fillConditionForEndpoint(entityRequestInfo, conditions);
        break;
      default:
        break;
    }
    
    return conditions;
  }
  
  private void fillConditionForTaskToExclude(List<String> conditions, List<String> typesToExclude)
  {
    String condition = ITask.TYPE + " NOT IN "  + EntityUtil.quoteIt(typesToExclude) + "";
     conditions.add(condition);
   }
  
  private void fillConditionForProcessEvent(List<String> conditions, Map<String, Object> entityRequestInfo)
  {
    List<String> eventTypes = (List<String>) entityRequestInfo.get(IProcessEvent.EVENT_TYPE);
    if(eventTypes != null && !eventTypes.isEmpty()) {
      String condition = IProcessEvent.EVENT_TYPE + " IN " + EntityUtil.quoteIt(eventTypes) + "";
      conditions.add(condition);
    }
    
    List<String> triggeringTypes = (List<String>) entityRequestInfo.get(IProcessEvent.TRIGGERING_TYPE);
    if(triggeringTypes != null && !triggeringTypes.isEmpty()) {
      String condition = IProcessEvent.TRIGGERING_TYPE + " IN " + EntityUtil.quoteIt(triggeringTypes) + "";
      conditions.add(condition);
    }

    String physicalCatalogId = (String) entityRequestInfo.get(IGetConfigDataWorkflowPaginationModel.PHYSICAL_CATALOG_ID);
    if(physicalCatalogId != null && !physicalCatalogId.isEmpty()) {
      String condition = "(" +IProcessEvent.PHYSICAL_CATALOG_IDS + " CONTAINS " + EntityUtil.quoteIt(physicalCatalogId) + " OR " + ISaveProcessEventModel.PHYSICAL_CATALOG_IDS
          + " = [])";
      conditions.add(condition);
    }
    
    /*Boolean isExecutable = (Boolean) entityRequestInfo.get(IGetConfigDataWorkflowPaginationModel.IS_EXECUTABLE);
    if(isExecutable != null) {
      String condition = IProcessEvent.IS_EXECUTABLE + " = " + isExecutable ;
      conditions.add(condition);
    }*/
    
    List<String> workflowTypes = (List<String>) entityRequestInfo.get(IGetConfigDataWorkflowPaginationModel.WORKFLOW_TYPES);
    if(workflowTypes!= null && !workflowTypes.isEmpty()) {
      String condition = IProcessEvent.WORKFLOW_TYPE + " IN " + EntityUtil.quoteIt(workflowTypes) + ""; 
      conditions.add(condition);
      
      if(workflowTypes.contains(WorkflowType.JMS_WORKFLOW.name())) {
       condition = "inE('Profile_JMS_Process_Link').size() = 0";
       conditions.add(condition);
      }
    }
    
    Boolean isTemplate = (Boolean) entityRequestInfo.get(IGetConfigDataWorkflowPaginationModel.IS_TEMPLATE);
    if(isTemplate!= null) {
      String condition = IGetConfigDataWorkflowPaginationModel.IS_TEMPLATE + " = " + isTemplate ;
      conditions.add(condition);
    }
    
    String organizationId = (String) entityRequestInfo.get(IGetConfigDataWorkflowPaginationModel.ORGANIZATION_ID);
    if (organizationId != null && !organizationId.isEmpty()) {
      String condition = "(" + IProcessEvent.ORGANIZATIONS_IDS + " CONTAINS "
          + EntityUtil.quoteIt(organizationId) + ")";
      conditions.add(condition);
    }
  }

  private void fillConditionForEndpoint(Map<String, Object> entityRequestInfo, List<String> conditions)
  {
    String physicalCatalogId =  (String) entityRequestInfo.get(IGetConfigDataEndpointPaginationModel.PHYSICAL_CATALOG_ID);
    if(physicalCatalogId != null && !physicalCatalogId.isEmpty()) {
      //endpointAttachedFlag = true;
      String condition = "(" + IEndpoint.PHYSICAL_CATALOGS + " CONTAINS " + EntityUtil.quoteIt(physicalCatalogId) + " OR " + IEndpoint.PHYSICAL_CATALOGS
          + " = [])";
      conditions.add(condition);
    }
  }
  private void fillConditionForAttributeTypeToExclude(List<String> conditions,
      List<String> typesToExclude)
  {
    String condition = IAttribute.TYPE + " NOT IN " + EntityUtil.quoteIt(typesToExclude) + "";
    conditions.add(condition);
  }
  
  private void fillConditionForAttributeIdsToExclude(List<String> conditions, List<String> idsToExclude)
  {
    String condition = IAttribute.CODE + " NOT IN " + EntityUtil.quoteIt(idsToExclude) + "";
    conditions.add(condition);
  }
  
  private void fillConditionForTagTypeToExclude(List<String> conditions,
      List<String> typesToExclude)
  {
    String condition = ITag.TAG_TYPE + " NOT IN " + EntityUtil.quoteIt(typesToExclude) + "";
    conditions.add(condition);
  }
  
  private void fillConditionForDisabledElement(List<String> conditions, Boolean isDisabled)
  {
    String condition = IAttribute.IS_DISABLED + " = " + isDisabled;
    if (isDisabled != true) {
      condition = "(" + condition + " or " + IAttribute.IS_DISABLED + " is null)";
    }
    
    conditions.add(condition);
  }
  
  private void fillConditionsForPropertyCollections(List<String> conditions,
      Map<String, Object> entityRequestInfo)
  {
    Boolean isForXRay = (Boolean) entityRequestInfo
        .get(IGetConfigDataPropertyCollectionPaginationModel.IS_FOR_XRAY);
    
    if (isForXRay != null) {
      String condition = IPropertyCollection.IS_FOR_X_RAY + " = " + isForXRay;
      conditions.add(condition);
    }
  }
  
  private void fillConditionsForVariantContext(List<String> conditions,
      Map<String, Object> entityRequestInfo)
  {
    Boolean isAttributeContext = (Boolean) entityRequestInfo.get(IS_ATTRIBUTE_CONTEXT);
    List<String> types = (List<String>) entityRequestInfo
        .get(IGetConfigDataEntityPaginationModel.TYPES);
    if (isAttributeContext == null) {
      return;
    }
    
    // Special handling for authorization mapping screen.
    if (types != null && types.size() > 0) {
      // Don't add condition for type.
    }
    else if (isAttributeContext) {
      String condition = IVariantContext.TYPE + " = \"" + CommonConstants.ATTRIBUTE_VARIANT_CONTEXT
          + "\"";
      conditions.add(condition);
    }
    else {
      String condition = IVariantContext.TYPE + " != \"" + CommonConstants.ATTRIBUTE_VARIANT_CONTEXT
          + "\"";
      conditions.add(condition);
    }
  }
  
  private void fillConditionForTab(List<String> conditions, Map<String, Object> entityRequestInfo,
      List<String> typesToExclude)
  {
    
    String condition = CommonConstants.CODE_PROPERTY + " NOT IN "
        + EntityUtil.quoteIt(typesToExclude) + "";
    conditions.add(condition);
  }
  
  private Map<String, Object> getDashboards(String searchColumn, String searchText,
      Map<String, Object> entities, String systemLevelId)
  {
    Map<String, Object> dashboardsRequestInfo = (Map<String, Object>) entities
        .get(IGetConfigDataEntityRequestModel.DASHBOARD_TABS);
    Map<String, Object> organizationsResponse = null;
    if (dashboardsRequestInfo != null) {
      organizationsResponse = fetchEntities(dashboardsRequestInfo, searchColumn, searchText,
          VertexLabelConstants.DASHBOARD_TAB, systemLevelId);
    }
    return organizationsResponse;
  }
  
  private Map<String, Object> getKPIs(String searchColumn, String searchText,
      Map<String, Object> entities, String systemLevelId)
  {
    Map<String, Object> kpisRequestInfo = (Map<String, Object>) entities
        .get(IGetConfigDataEntityRequestModel.KPIS);
    Map<String, Object> kpisResponse = null;
    if (kpisRequestInfo != null) {
      kpisResponse = fetchEntities(kpisRequestInfo, searchColumn, searchText,
          VertexLabelConstants.GOVERNANCE_RULE_KPI, systemLevelId);
    }
    return kpisResponse;
  }
  
  private Map<String, Object> getAuthorizationMappings(String searchColumn, String searchText,
      Map<String, Object> entities, String systemLevelId)
  {
    Map<String, Object> authorizationMappingsRequestInfo = (Map<String, Object>) entities
        .get(IGetConfigDataEntityRequestModel.AUTHORIZATION_MAPPING);
    Map<String, Object> authorizationMappingsResponse = null;
    if (authorizationMappingsRequestInfo != null) {
      authorizationMappingsResponse = fetchEntities(authorizationMappingsRequestInfo, searchColumn,
          searchText, VertexLabelConstants.AUTHORIZATION_MAPPING, systemLevelId);
    }
    return authorizationMappingsResponse;
  }
  
  private Map<String, Object> getUsers(String searchColumn, String searchText,
      Map<String, Object> entities, String systemLevelId)
  {
    Map<String, Object> rolesRequestInfo = (Map<String, Object>) entities
        .get(IGetConfigDataEntityRequestModel.USERS);
    Map<String, Object> rolesResponse = null;
    if (rolesRequestInfo != null) {
      rolesResponse = fetchEntities(rolesRequestInfo, searchColumn, searchText,
          VertexLabelConstants.ENTITY_TYPE_USER, systemLevelId);
      List<Map<String, Object>> entitiesList = (List<Map<String, Object>>) rolesResponse
          .get(IGetConfigDataEntityResponseModel.LIST);
      for (Map<String, Object> entity : entitiesList) {
        entity.put(CommonConstants.LABEL_PROPERTY,
            entity.remove(IUser.FIRST_NAME) + " " + entity.remove(IUser.LAST_NAME));
        
        entity.put(CommonConstants.IID_PROPERTY, entity.remove(IUser.USER_IID));
      }
    }
    return rolesResponse;
  }
  
  private Map<String, Object> getRootRelationships(String searchColumn, String searchText,
      Map<String, Object> entities, String systemLevelId)
  {
    Map<String, Object> rootRelationshipsRequestInfo = (Map<String, Object>) entities
        .get(IGetConfigDataEntityRequestModel.ROOT_RELAIONSHIPS);
    Map<String, Object> rootRelationshipsResponse = null;
    if (rootRelationshipsRequestInfo != null) {
      rootRelationshipsResponse = fetchEntities(rootRelationshipsRequestInfo, searchColumn,
          searchText, VertexLabelConstants.ROOT_RELATIONSHIP, systemLevelId);
    }
    return rootRelationshipsResponse;
  }
  
  private void fillConditionForRootRelationshipTypesToExclude(List<String> conditions,
      List<String> typesToExclude)
  {
    String condition = IAttribute.TYPE + " NOT IN " + EntityUtil.quoteIt(typesToExclude) + "";
    conditions.add(condition);
  }
}
