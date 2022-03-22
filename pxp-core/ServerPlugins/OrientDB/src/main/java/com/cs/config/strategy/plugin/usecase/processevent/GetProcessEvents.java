package com.cs.config.strategy.plugin.usecase.processevent;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.processevent.IProcessEvent;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.di.config.interactor.model.initializeworflowevent.IGetAllProcessEventsResponseModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetProcessEvents extends AbstractOrientPlugin {
  
  public GetProcessEvents(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> ids = (List<String>) requestMap.get(IConfigGetAllRequestModel.IDS);
    Long count = new Long(0);
    List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
    if(ids != null && !ids.isEmpty()) {
      getProcessByIds(ids, list);
    }
    else {
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
    StringBuilder searchQuery = EntityUtil.getSearchQuery(searchText, searchColumn);
    StringBuilder conditionQuery = EntityUtil.getConditionQuery(searchQuery);
    
    sortBy = EntityUtil.getLanguageConvertedField(sortBy);
    
    String query = "select from " + VertexLabelConstants.PROCESS_EVENT + conditionQuery
        + " order by " + sortBy + " " + sortOrder + " skip " + from + " limit " + size;
    list = executeQueryAndPrepareResponse(query);
    String countQuery = "select count(*) from " + VertexLabelConstants.PROCESS_EVENT
        + conditionQuery;
    count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    }
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IGetAllProcessEventsResponseModel.LIST, list);
    responseMap.put(IGetAllProcessEventsResponseModel.COUNT, count);
    return responseMap;
  }

  private void getProcessByIds(List<String> ids, List<Map<String, Object>> list)
  {
    ids.forEach(processId ->{
      try {
        Vertex processNode = UtilClass.getVertexById(processId, VertexLabelConstants.PROCESS_EVENT);

        Map<String, Object> processMap = UtilClass
            .getMapFromVertex(Arrays.asList(CommonConstants.CODE_PROPERTY, IProcessEvent.IP,
                IProcessEvent.PORT, IProcessEvent.QUEUE, IProcessEvent.WORKFLOW_TYPE), processNode);
        list.add(processMap);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
      
    });
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetProcessEvents/*" };
  }
  
  /**
   * @author Ajit
   * @param query
   * @return
   * @throws Exception
   */
  private List<Map<String, Object>> executeQueryAndPrepareResponse(String query) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> searchResults = graph.command(new OCommandSQL(query))
        .execute();
    List<Map<String, Object>> processesToReturn = new ArrayList<>();
    for (Vertex processNode : searchResults) {
      Map<String, Object> processMap = UtilClass
          .getMapFromVertex(Arrays.asList(CommonConstants.CODE_PROPERTY, IProcessEvent.LABEL,
              IProcessEvent.PROCESS_TYPE, IProcessEvent.CODE), processNode);
      processesToReturn.add(processMap);
    }
    return processesToReturn;
  }
}
