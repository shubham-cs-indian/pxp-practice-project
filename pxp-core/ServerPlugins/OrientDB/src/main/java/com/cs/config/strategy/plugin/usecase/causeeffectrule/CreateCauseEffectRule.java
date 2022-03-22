package com.cs.config.strategy.plugin.usecase.causeeffectrule;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.HashMap;
import java.util.Map;

public class CreateCauseEffectRule extends AbstractOrientPlugin {
  
  public CreateCauseEffectRule(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  @SuppressWarnings("unchecked")
  public Object execute(Map<String, Object> request) throws Exception
  {
    Map<String, Object> map = (Map<String, Object>) request.get("causeEffectRule");
    
    Map<String, Object> returnMap = new HashMap<>();
    OrientGraph graph = UtilClass.getGraph();
    
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(CommonConstants.CAUSE_EFFECT_RULE,
        CommonConstants.CODE_PROPERTY);
    
    Vertex dataRuleNode = UtilClass.createNode(map, vertexType);
    
    returnMap = UtilClass.getMapFromNode(dataRuleNode);
    graph.commit();
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateCauseEffectRule/*" };
  }
}
