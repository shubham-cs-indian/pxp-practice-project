package com.cs.config.strategy.plugin.usecase.standarduser;

import java.util.HashMap;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.user.util.UserUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class GetOrCreateUser extends AbstractOrientPlugin {
  
  public GetOrCreateUser(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> userMap = new HashMap<String, Object>();
    Map<String, Object> returnMap = new HashMap<String, Object>();
    
    userMap = (HashMap<String, Object>) requestMap.get("user");
    String userId = userMap.get(CommonConstants.ID_PROPERTY)
        .toString();
    Vertex userNode = null;
    try {
      userNode = UtilClass.getVertexByIndexedId(userId, VertexLabelConstants.ENTITY_STANDARD_USER);
      returnMap.putAll(UtilClass.getMapFromNode(userNode));
      UserUtils.getPreferredLanguages(returnMap, userNode);
    }
    catch (NotFoundException e) {
      String preferredUILanguage = (String) userMap.remove(IUserModel.PREFERRED_UI_LANGUAGE);
      String preferredDataLanguage = (String) userMap.remove(IUserModel.PREFERRED_DATA_LANGUAGE);
     
      OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
          VertexLabelConstants.ENTITY_STANDARD_USER, CommonConstants.CODE_PROPERTY,
          CommonConstants.USER_NAME_PROPERTY);
      
      userNode = UtilClass.createNode(userMap, vertexType);
      UserUtils.addOrUpdatePreferredLanguages(preferredUILanguage, preferredDataLanguage, userNode, returnMap);
      
      userNode.setProperty(CommonConstants.CODE_PROPERTY, userId);
      returnMap.putAll(UtilClass.getMapFromNode(userNode));
      UtilClass.getGraph().commit();
    }
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetOrCreateUser/*" };
  }
}
