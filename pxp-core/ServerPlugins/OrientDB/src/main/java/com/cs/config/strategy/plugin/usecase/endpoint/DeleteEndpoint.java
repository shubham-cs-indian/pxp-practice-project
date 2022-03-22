package com.cs.config.strategy.plugin.usecase.endpoint;

import com.cs.config.strategy.plugin.usecase.endpoint.util.EndpointUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({ "unchecked" })
public class DeleteEndpoint extends AbstractOrientPlugin {
  
  public DeleteEndpoint(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    List<String> profileIds = (List<String>) map.get("ids");
    String endpointType = (String) map.get(IEndpoint.ENDPOINT_TYPE);
    OrientGraph graph = UtilClass.getGraph();
    
    List<String> deletedIds = new ArrayList<>();
    for (String id : profileIds) {
      Vertex profileNode = UtilClass.getVertexByIndexedId(id, VertexLabelConstants.ENDPOINT);
      EndpointUtils.deleteEndpointMappings(profileNode);
      profileNode.remove();
      deletedIds.add(id);
    }
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put("success", deletedIds);
    
    graph.commit();
    
    return responseMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|DeleteEndpoint/*" };
  }
}
