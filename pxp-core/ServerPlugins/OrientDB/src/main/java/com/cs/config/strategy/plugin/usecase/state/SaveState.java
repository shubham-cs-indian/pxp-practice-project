package com.cs.config.strategy.plugin.usecase.state;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.util.ValidationUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.state.StateNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.Map;

public class SaveState extends AbstractOrientPlugin {
  
  public SaveState(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<String, Object>();
    Map<String, Object> userMap = (HashMap<String, Object>) requestMap.get("state");
    
    if (ValidationUtils.validateUserInfo(userMap)) {
      String userId = (String) userMap.get(CommonConstants.ID_PROPERTY);
      try {
        Vertex conditionNode = UtilClass.getVertexByIndexedId(userId, VertexLabelConstants.STATE);
        UtilClass.saveNode(userMap, conditionNode);
        returnMap.putAll(UtilClass.getMapFromNode(conditionNode));
      }
      catch (NotFoundException e) {
        throw new StateNotFoundException();
      }
    }
    UtilClass.getGraph()
        .commit();
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveState/*" };
  }
}
