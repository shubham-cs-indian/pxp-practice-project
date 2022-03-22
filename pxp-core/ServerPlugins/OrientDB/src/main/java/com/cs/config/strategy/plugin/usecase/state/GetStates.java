package com.cs.config.strategy.plugin.usecase.state;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetStates extends AbstractOrientPlugin {
  
  public GetStates(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    List<Map<String, Object>> dataRuleList = new ArrayList<>();
    
    Iterable<Vertex> resultIterable = UtilClass.getGraph()
        .command(new OCommandSQL("select from " + VertexLabelConstants.STATE))
        .execute();
    for (Vertex userNode : resultIterable) {
      Map<String, Object> map = new HashMap<String, Object>();
      map.putAll(UtilClass.getMapFromNode(userNode));
      dataRuleList.add(map);
    }
    
    Map<String, Object> response = new HashMap<>();
    response.put("list", dataRuleList);
    // UtilClass.getGraph().commit();
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetStates/*" };
  }
}
