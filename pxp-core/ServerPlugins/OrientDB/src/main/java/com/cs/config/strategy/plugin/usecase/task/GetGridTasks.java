package com.cs.config.strategy.plugin.usecase.task;

import com.cs.config.strategy.plugin.usecase.task.util.TasksUtil;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.task.ITask;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.task.IGetGridTasksResponseModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetGridTasks extends AbstractOrientPlugin {
  
  public GetGridTasks(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetGridTasks/*" };
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
    
    sortBy = EntityUtil.getLanguageConvertedField(sortBy);
    StringBuilder searchQuery = EntityUtil.getSearchQuery(searchText, searchColumn);
    StringBuilder typeQuery = UtilClass.getTypeQuery(types, ITask.TYPE);
    StringBuilder conditionQuery = EntityUtil.getConditionQuery(searchQuery, typeQuery);
    
    Long count = new Long(0);
    String query = "select from " + VertexLabelConstants.ENTITY_TYPE_TASK + conditionQuery
        + " order by " + sortBy + " " + sortOrder + " skip " + from + " limit " + size;
    list = executeQueryAndPrepareResponse(query);
    String countQuery = "select count(*) from " + VertexLabelConstants.ENTITY_TYPE_TASK
        + conditionQuery;
    count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    
    Map<String, Object> referencedConfigDetails = new HashMap<>();
    TasksUtil.fillReferencedConfigDetails(list, referencedConfigDetails);
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IGetGridTasksResponseModel.TASKS_LIST, list);
    responseMap.put(IGetGridTasksResponseModel.COUNT, count);
    responseMap.put(IGetGridTasksResponseModel.REFERENCED_TAGS, referencedConfigDetails);
    return responseMap;
  }
  
  private List<Map<String, Object>> executeQueryAndPrepareResponse(String query) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> searchResults = graph.command(new OCommandSQL(query))
        .execute();
    List<Map<String, Object>> taskToReturn = new ArrayList<>();
    for (Vertex taskNode : searchResults) {
      Map<String, Object> taskMap = TasksUtil.getTaskMapFromNode(taskNode);
      taskToReturn.add(taskMap);
    }
    return taskToReturn;
  }
}
