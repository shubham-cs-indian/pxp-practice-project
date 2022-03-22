package com.cs.config.strategy.plugin.usecase.causeeffectrule;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeleteCauseEffectRule extends AbstractOrientPlugin {
  
  public DeleteCauseEffectRule(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    List<String> dataRuleIds = new ArrayList<String>();
    
    dataRuleIds = (List<String>) map.get("ids");
    
    OrientGraph graph = UtilClass.getGraph();
    List<String> deletedIds = new ArrayList<>();
    for (String id : dataRuleIds) {
      Vertex userNode = UtilClass.getVertexByIndexedId(id, VertexLabelConstants.CAUSE_EFFECT_RULE);
      userNode.remove();
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
    return new String[] { "POST|DeleteCauseEffectRules/*" };
  }
}
