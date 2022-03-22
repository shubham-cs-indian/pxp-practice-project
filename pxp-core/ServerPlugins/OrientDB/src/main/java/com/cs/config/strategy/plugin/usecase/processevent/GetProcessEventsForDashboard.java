package com.cs.config.strategy.plugin.usecase.processevent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.processevent.util.ProcessEventUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.SystemLevelIds;
import com.cs.core.asset.services.CommonConstants;
import com.cs.core.config.interactor.entity.processevent.IProcessEvent;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.di.config.model.modeler.IWorkflowTaskRequestModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.WorkflowType;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class GetProcessEventsForDashboard extends AbstractOrientPlugin {
  
  public GetProcessEventsForDashboard(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetProcessEventsForDashboard/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    final StringBuilder conditionQuery = createConditionQuery(requestMap);
    final Long from = Long.valueOf(requestMap.get(IConfigGetAllRequestModel.FROM)
        .toString());
    final Long size = Long.valueOf(requestMap.get(IConfigGetAllRequestModel.SIZE)
        .toString());
    final String sortOrder = requestMap.get(IConfigGetAllRequestModel.SORT_ORDER)
        .toString();
    final String sortBy = EntityUtil
        .getLanguageConvertedField(requestMap.get(IConfigGetAllRequestModel.SORT_BY)
            .toString());
    
    final List<Map<String, Object>> list = executeQueryAndPrepareResponse(
        createFinalQuery(conditionQuery, from, size, sortOrder, sortBy),
        ProcessEventUtils.createConfigDetails());
    
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IGetConfigDataEntityResponseModel.FROM, from);
    responseMap.put(IGetConfigDataEntityResponseModel.SIZE, size);
    responseMap.put(IGetConfigDataEntityResponseModel.LIST, list);
    responseMap.put(IGetConfigDataEntityResponseModel.TOTAL_COUNT,
        EntityUtil.executeCountQueryToGetTotalCount(
            createCountQuery(conditionQuery)));
    return responseMap;
    
  }
  
  private StringBuilder createConditionQuery(Map<String, Object> requestMap)
  {
    StringBuilder eventTypesQuery = UtilClass.getTypeNotInQuery(
        Arrays.asList(EventType.APPLICATION.name()),
        IProcessEvent.EVENT_TYPE);
    
    StringBuilder workflowAndEntityTypeQuery = UtilClass.getTypeNotInQuery(
        Arrays.asList(WorkflowType.HIDDEN_WORKFLOW.name()), IWorkflowTaskRequestModel.WORKFLOW_TYPE);
    
    StringBuilder searchQuery = EntityUtil.getSearchQuery(
        requestMap.get(IConfigGetAllRequestModel.SEARCH_TEXT)
            .toString(),
        EntityUtil.getLanguageConvertedField(requestMap.get(IConfigGetAllRequestModel.SEARCH_COLUMN)
            .toString()));
    
    StringBuilder conditionQuery = EntityUtil.getConditionQuery(searchQuery, eventTypesQuery,
        workflowAndEntityTypeQuery);
    
    String excludeCIDsQuery = CommonConstants.CID_PROPERTY + " not in "
        + EntityUtil.quoteIt(SystemLevelIds.WORKFLOWS_TO_EXCLUDE_FROM_CONFIG_SCREEN);
    if (conditionQuery.length() == 0) {
      conditionQuery.append(" where " + excludeCIDsQuery);
    }
    else {
      conditionQuery.append(" and " + excludeCIDsQuery);
    }
    return conditionQuery;
  }
  
  private String createCountQuery(final StringBuilder conditionQuery)
  {
    return "select count(*) from " + VertexLabelConstants.PROCESS_EVENT + conditionQuery;
  }
  
  private String createFinalQuery(final StringBuilder conditionQuery, final Long from,
      final Long size, final String sortOrder, final String sortBy)
  {
    return "select from " + VertexLabelConstants.PROCESS_EVENT + conditionQuery + " order by "
        + sortBy + " " + sortOrder + " skip " + from + " limit " + size;
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
      
      returnMap.putAll(UtilClass.getMapFromVertex(
          Arrays.asList(IConfigEntityInformationModel.ID, IConfigEntityInformationModel.CODE,
              IConfigEntityInformationModel.LABEL, IConfigEntityInformationModel.TYPE),
          processEventNode));
      
      processEventsToReturn.add(returnMap);
    }
    return processEventsToReturn;
  }
}
