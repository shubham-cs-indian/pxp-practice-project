package com.cs.config.strategy.plugin.usecase.endpoint;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.config.interactor.exception.system.SystemNotFoundException;
import com.cs.core.config.interactor.model.endpoint.IGetEndpointForGridModel;
import com.cs.core.config.interactor.model.endpoint.IGetEnpointBySystemRequestModel;
import com.cs.core.config.interactor.model.endpoint.IGetGridEndpointsResponseModel;
import com.cs.core.exception.NotFoundException;
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
public class GetEndpointsBySystem extends AbstractOrientPlugin {
  
  public GetEndpointsBySystem(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetEndpointsBySystem/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> list = new ArrayList<>();
    String systemId = (String) requestMap.get(IGetEnpointBySystemRequestModel.SYSTEM_ID);
    Long from = Long.valueOf(requestMap.get(IGetEnpointBySystemRequestModel.FROM)
        .toString());
    Long size = Long.valueOf(requestMap.get(IGetEnpointBySystemRequestModel.SIZE)
        .toString());
    String sortBy = requestMap.get(IGetEnpointBySystemRequestModel.SORT_BY)
        .toString();
    String sortOrder = requestMap.get(IGetEnpointBySystemRequestModel.SORT_ORDER)
        .toString();
    String searchText = requestMap.get(IGetEnpointBySystemRequestModel.SEARCH_TEXT)
        .toString();
    String searchColumn = requestMap.get(IGetEnpointBySystemRequestModel.SEARCH_COLUMN)
        .toString();
    List<String> types = (List<String>) requestMap.get(IGetEnpointBySystemRequestModel.TYPES);
    
    searchColumn = EntityUtil.getLanguageConvertedField(searchColumn);
    sortBy = EntityUtil.getLanguageConvertedField(sortBy);
    
    StringBuilder typeQuery = UtilClass.getTypeQuery(types, IEndpoint.ENDPOINT_TYPE);
    
    Long count = new Long(0);
    Vertex systemNode = null;
    try {
      systemNode = UtilClass.getVertexById(systemId, VertexLabelConstants.SYSTEM);
    }
    catch (NotFoundException e) {
      throw new SystemNotFoundException();
    }
    
    String rid = systemNode.getId()
        .toString();
    String query = "select expand(in('Has_System')) from " + rid + " where " + searchColumn
        + " like '%" + searchText + "%'" + typeQuery + " order by " + sortBy + " " + sortOrder
        + " skip " + from + " limit " + size;
    
    list = executeQueryAndPrepareResponse(query);
    String countQuery = "select count(*) from (select expand(in('Has_System')) from " + rid
        + " where " + searchColumn + " like '%" + searchText + "%'" + typeQuery + ")";
    count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IGetGridEndpointsResponseModel.ENDPOINTS_LIST, list);
    responseMap.put(IGetGridEndpointsResponseModel.COUNT, count);
    return responseMap;
  }
  
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
      // EndpointUtils.getMapFromProfileNode(endpointNode, returnMap, new
      // ArrayList<>());
      endpointsToReturn.add(returnMap);
    }
    return endpointsToReturn;
  }
}
