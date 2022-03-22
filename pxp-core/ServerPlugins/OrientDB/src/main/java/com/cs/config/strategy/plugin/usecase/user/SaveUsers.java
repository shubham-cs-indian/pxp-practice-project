package com.cs.config.strategy.plugin.usecase.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.user.util.UserUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.util.ValidationUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.user.IUser;
import com.cs.core.config.interactor.exception.user.BulkSaveUsersFailedException;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.user.IBulkSaveUsersResponseModel;
import com.cs.core.config.interactor.model.user.IGetGridUsersResponseModel;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class SaveUsers extends AbstractOrientPlugin {
  
  public SaveUsers(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  public static List<String> fieldsToFetch = Arrays.asList(IRole.ID, IRole.LABEL, IRole.CODE);
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> listOfUsers = (List<Map<String, Object>>) requestMap
        .get(IListModel.LIST);
    List<Map<String, Object>> listOfSuccessSavedUsers = new ArrayList<>();
    Map<String, Object> referencedRoles = new HashMap<>();
    IExceptionModel failure = new ExceptionModel();
    List<Map<String, Object>> auditInfoList = new ArrayList<>();
    
    for (Map<String, Object> user : listOfUsers) {
      try {
        Map<String, Object> userMap = new HashMap<String, Object>();
        
        if (ValidationUtils.validateUserInfo(user)) {
          String userId = (String) user.get(CommonConstants.ID_PROPERTY);
          String preferredUILanguage = (String) user.remove(IUserModel.PREFERRED_UI_LANGUAGE);
          String preferredDataLanguage = (String) user.remove(IUserModel.PREFERRED_DATA_LANGUAGE);
          
          Vertex userNode = UtilClass.getVertexById(userId, VertexLabelConstants.ENTITY_TYPE_USER);
          if (user.get("password") != null && user.get("password").toString().isEmpty()) {
            user.put("password", userNode.getProperty("password"));
          }
          
          UtilClass.saveNode(user, userNode, new ArrayList<>());
          UserUtils.addOrUpdatePreferredLanguages(preferredUILanguage, preferredDataLanguage, userNode, userMap);

          userMap.putAll(UtilClass.getMapFromNode(userNode));
          userMap.remove(IUser.PASSWORD);
          addRoleTypeFieldToUserMap(userNode, userMap, referencedRoles);
          

          AuditLogUtils.fillAuditLoginfo(auditInfoList, userNode, Entities.USERS, Elements.UNDEFINED);
        }
        listOfSuccessSavedUsers.add(userMap);
      }
      catch (PluginException ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, null, null);
      }
      catch (Exception ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, null, null);
      }
    }
    if (listOfSuccessSavedUsers.isEmpty()) {
      throw new BulkSaveUsersFailedException(failure.getExceptionDetails(),
          failure.getDevExceptionDetails());
    }
    UtilClass.getGraph()
        .commit();
    
    Map<String, Object> successMap = new HashMap<>();
    successMap.put(IGetGridUsersResponseModel.USERS_LIST, listOfSuccessSavedUsers);
    successMap.put(IGetGridUsersResponseModel.REFERENCED_ROLES, referencedRoles);
    
    Map<String, Object> bulkSaveUsersResponse = new HashMap<String, Object>();
    bulkSaveUsersResponse.put(IBulkSaveUsersResponseModel.SUCCESS, successMap);
    bulkSaveUsersResponse.put(IBulkSaveUsersResponseModel.FAILURE, failure);
    bulkSaveUsersResponse.put(IBulkSaveUsersResponseModel.AUDIT_LOG_INFO, auditInfoList);
    return bulkSaveUsersResponse;
  }
  
  private void addRoleTypeFieldToUserMap(Vertex userVertex, Map<String, Object> userMap,
      Map<String, Object> referencedRoles)
  {
    
    Iterator<Edge> userOutRelationships = userVertex
        .getEdges(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_USER_IN)
        .iterator();
    
    if (userOutRelationships.hasNext()) {
      Vertex roleNode = userOutRelationships.next()
          .getVertex(Direction.IN);
      String roleId = UtilClass.getCodeNew(roleNode);
      if (!referencedRoles.containsKey(roleId)) {
        referencedRoles.put(roleId, UtilClass.getMapFromVertex(fieldsToFetch, roleNode));
      }
      userMap.put(IUserModel.ROLE_ID, roleId);
    }
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveUsers/*" };
  }
}
