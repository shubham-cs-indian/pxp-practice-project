package com.cs.config.strategy.plugin.usecase.system;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.system.ISystemModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
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

public class GetPaginatedSystems extends AbstractOrientPlugin {
  
  public static final List<String> FIELDS_TO_FETCH = Arrays.asList(ISystemModel.ICON,
      ISystemModel.ID, ISystemModel.LABEL);
  
  public GetPaginatedSystems(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetPaginatedSystems/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> list = new ArrayList<>();
    
    Long from = Long.valueOf(requestMap.get(IConfigGetAllRequestModel.FROM)
        .toString());
    Long size = Long.valueOf(requestMap.get(IConfigGetAllRequestModel.SIZE)
        .toString());
    String searchText = requestMap.get(IConfigGetAllRequestModel.SEARCH_TEXT)
        .toString();
    String label = EntityUtil.getLanguageConvertedField(ISystemModel.LABEL);
    
    StringBuilder searchQuery = EntityUtil.getSearchQuery(searchText, label);
    StringBuilder conditionQuery = EntityUtil.getConditionQuery(searchQuery);
    String query = "select from " + VertexLabelConstants.SYSTEM + conditionQuery + " skip " + from
        + " limit " + size;
    list = executeQueryAndPrepareResponse(query);
    
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IListModel.LIST, list);
    return responseMap;
  }
  
  /**
   * Osho
   *
   * @param query
   * @param isSearchQuery
   * @return List<Map<String, Object> listOfSystemsToBeReturned
   * @throws Exception
   */
  private List<Map<String, Object>> executeQueryAndPrepareResponse(String query) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> searchResults = graph.command(new OCommandSQL(query))
        .execute();
    List<Map<String, Object>> systemsToReturn = new ArrayList<>();
    for (Vertex searchResult : searchResults) {
      Map<String, Object> systemMap = UtilClass.getMapFromNode(searchResult);
      systemsToReturn.add(systemMap);
    }
    return systemsToReturn;
  }
}
