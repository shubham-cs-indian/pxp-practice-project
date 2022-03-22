package com.cs.config.strategy.plugin.usecase.causeeffectrule;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.util.ValidationUtils;
import com.cs.constants.CommonConstants;
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

public class SaveCauseEffectRule extends AbstractOrientPlugin {
  
  public SaveCauseEffectRule(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    HashMap<String, Object> userMap = new HashMap<String, Object>();
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    
    userMap = (HashMap<String, Object>) map.get("causeEffectRule");
    
    OrientGraph graph = UtilClass.getGraph();
    
    if (ValidationUtils.validateUserInfo(userMap)) {
      String userId = (String) userMap.get(CommonConstants.ID_PROPERTY);
      try {
        Vertex conditionNode = UtilClass.getVertexByIndexedId(userId,
            VertexLabelConstants.CAUSE_EFFECT_RULE);
        UtilClass.saveNode(userMap, conditionNode);
        returnMap.putAll(UtilClass.getMapFromNode(conditionNode));
      }
      catch (NotFoundException e) {
        throw new CauseEffectRuleNotFoundException("CAUSE_EFFECT_RULE_NOT_FOUND",
            ErrorCodes.CAUSE_EFFECT_RULE_NOT_FOUND_ON_SAVE);
      }
    }
    graph.commit();
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveCauseEffectRule/*" };
  }
}
