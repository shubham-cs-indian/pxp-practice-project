package com.cs.config.strategy.plugin.usecase.causeeffectrule;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.exception.causeeffectrule.CauseEffectRuleNotFoundException;
import com.cs.core.exception.ErrorCodes;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.HashMap;
import java.util.Map;

public class GetCauseEffectRule extends AbstractOrientPlugin {
  
  public GetCauseEffectRule(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> request) throws Exception
  {
    String userId = null;
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    
    Map<String, Object> map = (HashMap<String, Object>) request.get("causeEffectRule");
    userId = (String) map.get("id");
    
    OrientGraph graph = UtilClass.getGraph();
    try {
      Vertex userNode = UtilClass.getVertexByIndexedId(userId,
          VertexLabelConstants.CAUSE_EFFECT_RULE);
      returnMap.putAll(UtilClass.getMapFromNode(userNode));
    }
    catch (NotFoundException e) {
      throw new CauseEffectRuleNotFoundException("CAUSE_EFFECT_RULE_NOT_FOUND",
          ErrorCodes.CAUSE_EFFECT_RULE_NOT_FOUND_ON_GET);
    }
    graph.commit();
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetCauseEffectRule/*" };
  }
}
