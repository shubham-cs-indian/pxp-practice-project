package com.cs.config.strategy.plugin.usecase.onboardinguser;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetOrCreateStandardOnboardingUser extends AbstractOrientPlugin {
  
  public GetOrCreateStandardOnboardingUser(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> mapdata) throws Exception
  {
    HashMap<String, Object> userMap = new HashMap<String, Object>();
    
    userMap = (HashMap<String, Object>) mapdata.get("onboardingUser");
    OrientGraph graph = UtilClass.getGraph();
    String userId = userMap.get(CommonConstants.ID_PROPERTY)
        .toString();
    
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    Vertex userNode = null;
    try {
      userNode = UtilClass.getVertexByIndexedId(userId, VertexLabelConstants.ONBOARDING_USER);
      returnMap.putAll(UtilClass.getMapFromNode(userNode));
    }
    catch (NotFoundException e) {
      OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
          VertexLabelConstants.ONBOARDING_USER, CommonConstants.CODE_PROPERTY,
          CommonConstants.USER_NAME_PROPERTY);
      userNode = UtilClass.createNode(userMap, vertexType, new ArrayList<>());
      userNode.setProperty(CommonConstants.CODE_PROPERTY, userId);
      returnMap.putAll(UtilClass.getMapFromNode(userNode));
      graph.commit();
    }
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetOrCreateStandardOnboardingUser/*" };
  }
}
