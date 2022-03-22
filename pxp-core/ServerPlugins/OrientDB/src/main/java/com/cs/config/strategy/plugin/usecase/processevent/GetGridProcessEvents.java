package com.cs.config.strategy.plugin.usecase.processevent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.processevent.util.ProcessEventUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.processevent.IProcessEvent;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.grid.IWorkflowGridConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.grid.IWorkflowGridFilterModel;
import com.cs.core.config.interactor.model.processevent.IGetProcessEventModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.di.config.interactor.model.initializeworflowevent.IGetGridProcessEventsResponseModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings("unchecked")
public class GetGridProcessEvents extends AbstractOrientPlugin {
  
  public GetGridProcessEvents(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetGridProcessEvents/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> list = new ArrayList<>();
    
    Long from = Long.valueOf(requestMap.get(IConfigGetAllRequestModel.FROM)
        .toString());
    Long size = Long.valueOf(requestMap.get(IConfigGetAllRequestModel.SIZE)
        .toString());
    String sortBy = requestMap.get(IConfigGetAllRequestModel.SORT_BY)
        .toString();
    String sortOrder = requestMap.get(IConfigGetAllRequestModel.SORT_ORDER)
        .toString();
    String searchText = requestMap.get(IConfigGetAllRequestModel.SEARCH_TEXT)
        .toString();
    String searchColumn = requestMap.get(IConfigGetAllRequestModel.SEARCH_COLUMN)
        .toString();
    List<String> types = (List<String>) requestMap.get(IConfigGetAllRequestModel.TYPES);
    
    String workflowType = (String) requestMap.get(IConfigGetAllRequestModel.WORKFLOW_TYPE);
    String entityType = (String) requestMap.get(IConfigGetAllRequestModel.ENTITY_TYPE);
   final Map<String,Object> workflowGridfilterMap = (Map<String, Object>) requestMap.get(IWorkflowGridConfigGetAllRequestModel.WORKFLOW_GRID_FILTER_MODEL);
    searchColumn = EntityUtil.getLanguageConvertedField(searchColumn);
    sortBy = EntityUtil.getLanguageConvertedField(sortBy);
    StringBuilder searchQuery = EntityUtil.getSearchQuery(searchText, searchColumn);
    StringBuilder workflowAndEntityTypeQuery ;
    StringBuilder typeQuery = UtilClass.getTypeQuery(types, IProcessEvent.PROCESS_TYPE);
    if(null != workflowGridfilterMap) {
      workflowAndEntityTypeQuery = workflowEntityTypeFilterQuery(workflowGridfilterMap);
    }else {
      workflowAndEntityTypeQuery = UtilClass.getWorkflowAndEntityTypeQuery(workflowType, entityType);
    }
    
   UtilClass.getWorkflowAndEntityTypeQuery(workflowType, entityType);
    StringBuilder conditionQuery = EntityUtil.getConditionQuery(searchQuery, typeQuery, workflowAndEntityTypeQuery);    
    String excludeCIDsQuery = CommonConstants.CODE_PROPERTY + " not in "
        + EntityUtil.quoteIt(SystemLevelIds.WORKFLOWS_TO_EXCLUDE_FROM_CONFIG_SCREEN);
    if (conditionQuery.length() == 0) {
      conditionQuery.append(" where " + excludeCIDsQuery);
    }
    else {
      conditionQuery.append(" and " + excludeCIDsQuery);
    }
    
    Long count = new Long(0);
    String query = "select from " + VertexLabelConstants.PROCESS_EVENT + conditionQuery
        + " order by " + sortBy + " " + sortOrder + " skip " + from + " limit " + size;
    Map<String, Object> configMap = ProcessEventUtils.createConfigDetails();
    list = executeQueryAndPrepareResponse(query, configMap);

    String countQuery = "select count(*) from " + VertexLabelConstants.PROCESS_EVENT
        + conditionQuery;
    count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IGetProcessEventModel.CONFIG_DETAILS, configMap);
    responseMap.put(IGetGridProcessEventsResponseModel.PROCESS_EVENTS_LIST, list);
    responseMap.put(IGetGridProcessEventsResponseModel.COUNT, count);
    
    return responseMap;
  }
  
  private List<Map<String, Object>> executeQueryAndPrepareResponse(String query,
      Map<String, Object> configMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> searchResults = graph.command(new OCommandSQL(query))
        .execute();
    List<Map<String, Object>> processEventsToReturn = new ArrayList<>();
    for (Vertex processEventNode : searchResults) {
      Map<String, Object> returnMap = new HashMap<>();
      returnMap.putAll(UtilClass.getMapFromNode(processEventNode));
      ProcessEventUtils.getConfigInformationForProcessInGrid(processEventNode,
          (Map<String, Object>) returnMap, configMap);
      processEventsToReturn.add(returnMap);
    }
    return processEventsToReturn;
  }
  
  private StringBuilder workflowEntityTypeFilterQuery(Map<String, Object> filterMap)
  {
    StringBuilder workflowAndEntityTypeQuery;
    List<String> groupOfWrolflowType =  (List<String>) filterMap.get(IWorkflowGridFilterModel.WORKFLOW_TYPE);
    List<String> groupOfEventType =  (List<String>) filterMap.get(IWorkflowGridFilterModel.EVENT_TYPE);
    List<String> groupOfPhysicalCatalogIds =  (List<String>) filterMap.get(IWorkflowGridFilterModel.PHYSICAL_CATALOG_IDS);
    List<String> groupOfTriggeringType =  (List<String>) filterMap.get(IWorkflowGridFilterModel.TRIGGERINGTYPE);
    List<String> groupOfOrganizationIds =  (List<String>) filterMap.get(IWorkflowGridFilterModel.ORGANIZATION_IDS);
    List<String> groupOfTimerDefinitionType =  (List<String>) filterMap.get(IWorkflowGridFilterModel.TIMER_DEFINITION_TYPE);
    List<Boolean> isExecutable = (List<Boolean>)filterMap.get(IWorkflowGridFilterModel.ACTIVATION);
    String timerStartExpression = (String)filterMap.get(IWorkflowGridFilterModel.TIMER_START_EXPRESSION);
    workflowAndEntityTypeQuery = UtilClass.getWorkflowAndEntityTypeQuery(groupOfWrolflowType,
        groupOfEventType, groupOfPhysicalCatalogIds, groupOfTriggeringType,
        groupOfTimerDefinitionType, isExecutable, timerStartExpression, groupOfOrganizationIds);
    return workflowAndEntityTypeQuery;
  }
}
