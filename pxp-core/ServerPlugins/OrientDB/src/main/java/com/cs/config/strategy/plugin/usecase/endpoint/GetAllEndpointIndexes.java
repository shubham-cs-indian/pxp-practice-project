package com.cs.config.strategy.plugin.usecase.endpoint;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
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

public class GetAllEndpointIndexes extends AbstractOrientPlugin {
  
  public GetAllEndpointIndexes(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    
    List<String> profilesList = new ArrayList<>();
    
    Iterable<Vertex> resultIterable = null;
    try {
      resultIterable = graph
          .command(new OCommandSQL(
              "select from " + VertexLabelConstants.ENDPOINT + " where indexName IS NOT NULL"))
          .execute();
      for (Vertex profileNode : resultIterable) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.putAll(UtilClass.getMapFromVertex(Arrays.asList(IEndpoint.INDEX_NAME), profileNode));
        profilesList.add((String) map.get(IEndpoint.INDEX_NAME));
      }
    }
    catch (Exception e) {
      // In case when the Endpoint is not created and during initialize it tries
      // to get all indexes
    }
    Map<String, Object> response = new HashMap<>();
    response.put("ids", profilesList);
    graph.commit();
    
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllEndpointIndexes/*" };
  }
}
