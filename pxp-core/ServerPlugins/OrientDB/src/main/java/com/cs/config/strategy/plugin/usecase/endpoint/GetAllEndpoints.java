package com.cs.config.strategy.plugin.usecase.endpoint;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GetAllEndpoints extends AbstractOrientPlugin {
  
  public GetAllEndpoints(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    String endpointType = (String) requestMap.get(IEndpoint.ENDPOINT_TYPE);
    List<Map<String, Object>> profilesList = new ArrayList<>();
    
    String query;
    if (endpointType == null) {
      query = "select from " + VertexLabelConstants.ENDPOINT + " order by "
          + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc";
    }
    else {
      query = "select from " + VertexLabelConstants.ENDPOINT + " where " + IEndpoint.ENDPOINT_TYPE
          + "= \"" + endpointType + "\" order by "
          + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc";
    }
    
    Iterable<Vertex> resultIterable = graph.command(new OCommandSQL(query))
        .execute();
    for (Vertex profileNode : resultIterable) {
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.putAll(UtilClass.getMapFromVertex(Arrays.asList(CommonConstants.CODE_PROPERTY,
          IEndpoint.LABEL, IEndpoint.ENDPOINT_TYPE, IEndpoint.CODE), profileNode));
      Iterator<Vertex> systemVertices = profileNode
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_SYSTEM)
          .iterator();
      if (systemVertices.hasNext()) {
        String systemId = systemVertices.next()
            .getProperty(CommonConstants.CODE_PROPERTY);
        map.put(IEndpoint.SYSTEM_ID, systemId);
      }
      
      profilesList.add(map);
    }
    
    Map<String, Object> response = new HashMap<>();
    response.put("list", profilesList);
    graph.commit();
    
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllEndpoints/*" };
  }
}
