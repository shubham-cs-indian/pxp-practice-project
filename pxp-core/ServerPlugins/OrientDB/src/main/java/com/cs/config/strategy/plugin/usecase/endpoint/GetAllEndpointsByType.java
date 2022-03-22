package com.cs.config.strategy.plugin.usecase.endpoint;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.config.interactor.model.endpoint.IGetAllEndpointsByTypeRequestModel;
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

public class GetAllEndpointsByType extends AbstractOrientPlugin {
  
  public GetAllEndpointsByType(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    String searchText = (String) requestMap.get(IGetAllEndpointsByTypeRequestModel.SEARCH_TEXT);
    Long from = Long.valueOf(requestMap.get(IGetAllEndpointsByTypeRequestModel.FROM)
        .toString());
    Long size = Long.valueOf(requestMap.get(IGetAllEndpointsByTypeRequestModel.SIZE)
        .toString());
    String endpointType = (String) requestMap.get(IGetAllEndpointsByTypeRequestModel.ENDPOINT_TYPE);
    List<Map<String, Object>> endpointsList = new ArrayList<>();
    
    String query;
    
    query = "select from " + VertexLabelConstants.ENDPOINT + " where " + IEndpoint.ENDPOINT_TYPE
        + " = '" + endpointType + "' AND "
        + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " like '%"
        + searchText + "%' order by "
        + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc skip " + from
        + " limit " + size;
    
    Iterable<Vertex> resultIterable = graph.command(new OCommandSQL(query))
        .execute();
    for (Vertex profileNode : resultIterable) {
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.putAll(
          UtilClass.getMapFromVertex(Arrays.asList(IEndpoint.LABEL, IEndpoint.CODE), profileNode));
      map.put(IEndpoint.ID, profileNode.getProperty(CommonConstants.CODE_PROPERTY));
      endpointsList.add(map);
    }
    
    Map<String, Object> response = new HashMap<>();
    response.put("list", endpointsList);
    graph.commit();
    
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllEndpointsByType/*" };
  }
}
