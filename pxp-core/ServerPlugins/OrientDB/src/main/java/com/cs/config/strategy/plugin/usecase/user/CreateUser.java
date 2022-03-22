package com.cs.config.strategy.plugin.usecase.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.user.util.UserUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.util.ValidationUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.user.IUser;
import com.cs.core.config.interactor.exception.user.UserNameCannotBeEmptyException;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.exception.DuplicateUserNameException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class CreateUser extends AbstractOrientPlugin {
  
  public CreateUser(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<String, Object>();
    Map<String, Object> userMap = (Map<String, Object>) requestMap.get("user");
 
    String userName = (String) userMap.get("userName");
    userName = userName.trim();
    if (userName.isEmpty()) {
      throw new UserNameCannotBeEmptyException();
    }
    
    // To check if username already exist
    Iterable<Vertex> resultIterable = UtilClass.getGraph()
        .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_USER + " where "
            + CommonConstants.USER_NAME_PROPERTY + " = '" + userName + "'"))
        .execute();
    Iterator<Vertex> iterator = resultIterable.iterator();
    
    while (iterator.hasNext()) {
      throw new DuplicateUserNameException();
    }
    
    userMap.put("userName", userName);
    
    if (ValidationUtils.validateUserInfo(userMap)) {
      String preferredUILanguage = (String) userMap.remove(IUserModel.PREFERRED_UI_LANGUAGE);
      String preferredDataLanguage = (String) userMap.remove(IUserModel.PREFERRED_DATA_LANGUAGE);
      OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
          VertexLabelConstants.ENTITY_TYPE_USER, CommonConstants.CODE_PROPERTY);
      Vertex userNode = UtilClass.createNode(userMap, vertexType, new ArrayList<>());
      
      UserUtils.addOrUpdatePreferredLanguages(preferredUILanguage, preferredDataLanguage, userNode, returnMap);
   
      /*if (((String) userMap.get(CommonConstants.ID_PROPERTY)).equals(CommonConstants.ADMIN_USER_ID)) {
        userNode.setProperty(CommonConstants.CODE_PROPERTY, CommonConstants.ADMIN_USER_ID);
      }*/
      returnMap.putAll(UtilClass.getMapFromNode(userNode));
      returnMap.remove(IUser.PASSWORD);
      AuditLogUtils.fillAuditLoginfo(returnMap, userNode, Entities.USERS, Elements.UNDEFINED);
      UtilClass.getGraph().commit();
    }
    return returnMap;
  }

 
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateUser/*" };
  }
}
