package com.cs.config.strategy.plugin.usecase.user;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.sso.ISSOConfiguration;
import com.cs.core.config.interactor.entity.user.IUser;
import com.cs.core.config.interactor.exception.user.BackgroundUserCannotLoggedInException;
import com.cs.core.config.interactor.model.user.IGetUserValidateModel;
import com.cs.core.config.interactor.model.user.IValidateUserRequestModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class GetValidateUser extends AbstractOrientPlugin {
  
  public GetValidateUser(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    String domain = "";
    Map<String, String> validatationMap = (Map<String, String>) requestMap.get(IValidateUserRequestModel.VALIDATION_MAP);
    
    StringBuilder conditionQuery = new StringBuilder();
    for (Entry<String, String> entry : validatationMap.entrySet()) {
      if (conditionQuery.length() == 0) {
        conditionQuery.append(" where " + entry.getKey() + "=" + EntityUtil.quoteIt(entry.getValue()));
      }
      else {
        conditionQuery.append(" and " + entry.getKey() + "=" + EntityUtil.quoteIt(entry.getValue()));
      }
    }
    
    Iterable<Vertex> userIterable = UtilClass.getGraph()
        .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_USER + conditionQuery)).execute();
    
    Iterator<Vertex> iterator = userIterable.iterator();
    
    String idp = null;
    String userId = null;
    String username = null;
    String type = CommonConstants.NON_SSO_USER;
    Boolean isValid = false;
    Boolean isBackgroundUser = false;
    
    if (iterator.hasNext()) {
      isValid = true;
      Vertex user = iterator.next();
      userId = user.getProperty(CommonConstants.CODE_PROPERTY);
      username = user.getProperty(IGetUserValidateModel.USER_NAME);
      
      isBackgroundUser = user.getProperty(IUser.IS_BACKGROUND_USER);
      
      if (isBackgroundUser != null && isBackgroundUser) {
        throw new BackgroundUserCannotLoggedInException();
      }
      
      String query = "select expand(out('" + RelationshipLabelConstants.RELATIONSHIPLABEL_USER_IN + "').in('"
          + RelationshipLabelConstants.ORGANIZATION_ROLE_LINK + "').out('" + RelationshipLabelConstants.ORGANIZATION_SSO_LINK + "'))"
          + " from " + user.getId();
      
      Iterable<Vertex> resultIterable = UtilClass.getGraph().command(new OCommandSQL(query)).execute();
      
      if (username.indexOf("@") != -1) {
        domain = username.substring(username.indexOf("@") + 1);
      }
      
      for (Vertex vertex : resultIterable) {
        String property = vertex.getProperty(ISSOConfiguration.DOMAIN);
        if (domain.equals(property)) {
          idp = vertex.getProperty(ISSOConfiguration.IDP);
          type = vertex.getProperty(ISSOConfiguration.TYPE);
          break;
        }
      }
    }
    
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IGetUserValidateModel.USER_ID, userId);
    returnMap.put(IGetUserValidateModel.USER_NAME, username);
    returnMap.put(IGetUserValidateModel.IDP, idp);
    returnMap.put(IGetUserValidateModel.IS_VALID_USER, isValid);
    returnMap.put(IGetUserValidateModel.TYPE, type);
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetValidateUser/*" };
  }
}
