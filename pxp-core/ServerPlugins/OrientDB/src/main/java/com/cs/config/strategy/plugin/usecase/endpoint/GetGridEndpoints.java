package com.cs.config.strategy.plugin.usecase.endpoint;

import com.cs.config.strategy.plugin.usecase.endpoint.util.EndpointUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.config.interactor.model.endpoint.IGetEndpointForGridModel;
import com.cs.core.config.interactor.model.endpoint.IGetGridEndpointsResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
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
public class GetGridEndpoints extends AbstractOrientPlugin {
  
  public GetGridEndpoints(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetGridEndpoints/*" };
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
    
    StringBuilder searchQuery = EntityUtil.getSearchQuery(searchText, searchColumn);
    
    sortBy = EntityUtil.getLanguageConvertedField(sortBy);
    
    StringBuilder typeQuery = UtilClass.getTypeQueryWithoutANDOperator(types,
        IEndpoint.ENDPOINT_TYPE);
    
    StringBuilder conditionQuery = EntityUtil.getConditionQuery(searchQuery, typeQuery);
    
    Long count = new Long(0);
    String query = "select from " + VertexLabelConstants.ENDPOINT + conditionQuery + " order by "
        + sortBy + " " + sortOrder + " skip " + from + " limit " + size;
    
    list = executeQueryAndPrepareResponse(query);
    String countQuery = "select count(*) from " + VertexLabelConstants.ENDPOINT + conditionQuery;
    count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    
    Map<String, Object> responseMap = new HashMap<>();
    EndpointUtils.fillReferencedConfigDetails(list, responseMap);
    responseMap.put(IGetGridEndpointsResponseModel.ENDPOINTS_LIST, list);
    responseMap.put(IGetGridEndpointsResponseModel.COUNT, count);
    return responseMap;
  }
  
  /**
   * Osho
   *
   * @param query
   * @param isSearchQuery
   * @return List<Map<String, Object> listOfAttributesToBeReturned
   * @throws Exception
   */
  private List<Map<String, Object>> executeQueryAndPrepareResponse(String query) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> searchResults = graph.command(new OCommandSQL(query))
        .execute();
    List<Map<String, Object>> endpointsToReturn = new ArrayList<>();
    for (Vertex endpointNode : searchResults) {
      Map<String, Object> returnMap = new HashMap<>();
      Map<String, Object> endpointMap = UtilClass.getMapFromVertex(new ArrayList<>(), endpointNode);
      returnMap.put(IGetEndpointForGridModel.ENDPOINT, endpointMap);
      EndpointUtils.getMapFromProfileNode(endpointNode, returnMap, new ArrayList<>());
      endpointsToReturn.add(returnMap);
    }
    return endpointsToReturn;
  }
}
