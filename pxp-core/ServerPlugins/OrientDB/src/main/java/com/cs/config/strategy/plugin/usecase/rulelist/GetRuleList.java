package com.cs.config.strategy.plugin.usecase.rulelist;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.exception.rulelist.RuleListNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.Map;

public class GetRuleList extends AbstractOrientPlugin {
  
  public GetRuleList(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    HashMap<String, Object> map = new HashMap<String, Object>();
    
    String ruleListId = null;
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    
    map = (HashMap<String, Object>) requestMap.get("ruleList");
    ruleListId = (String) map.get("id");
    
    try {
      Vertex ruleListNode = UtilClass.getVertexByIndexedId(ruleListId,
          VertexLabelConstants.RULE_LIST);
      returnMap.putAll(UtilClass.getMapFromNode(ruleListNode));
    }
    catch (NotFoundException e) {
      throw new RuleListNotFoundException();
    }
    UtilClass.getGraph()
        .commit();
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetRuleList/*" };
  }
}
