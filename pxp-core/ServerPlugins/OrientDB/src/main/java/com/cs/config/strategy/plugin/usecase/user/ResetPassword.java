package com.cs.config.strategy.plugin.usecase.user;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.user.UserNotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.Map;

public class ResetPassword extends AbstractOrientPlugin {
  
  public ResetPassword(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|ResetPassword/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> userMap = (Map<String, Object>) requestMap.get("user");
    
    String userId = (String) userMap.get(CommonConstants.ID_PROPERTY);
    
    Vertex userNode = UtilClass.getVertexById(userId, VertexLabelConstants.ENTITY_TYPE_USER);
    
    if (userNode == null) {
      throw new UserNotFoundException();
    }
    
    Object password = userMap.get("password");
    if (password != null) {
      userNode.setProperty("password", password);
    }
    
    HashMap<String, Object> returnMap = UtilClass.getMapFromNode(userNode);
    UtilClass.getGraph()
        .commit();
    
    return returnMap;
  }
}
