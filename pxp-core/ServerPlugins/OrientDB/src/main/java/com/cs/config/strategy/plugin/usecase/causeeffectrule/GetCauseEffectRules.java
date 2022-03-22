package com.cs.config.strategy.plugin.usecase.causeeffectrule;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
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

public class GetCauseEffectRules extends AbstractOrientPlugin {
  
  public GetCauseEffectRules(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    
    List<Map<String, Object>> dataRuleList = new ArrayList<>();
    
    Iterable<Vertex> resultIterable = graph
        .command(new OCommandSQL("select from " + VertexLabelConstants.CAUSE_EFFECT_RULE))
        .execute();
    for (Vertex userNode : resultIterable) {
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.putAll(UtilClass.getMapFromNode(userNode));
      dataRuleList.add(map);
    }
    
    Map<String, Object> response = new HashMap<>();
    response.put("list", dataRuleList);
    graph.commit();
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetCauseEffectRules/*" };
  }
}
