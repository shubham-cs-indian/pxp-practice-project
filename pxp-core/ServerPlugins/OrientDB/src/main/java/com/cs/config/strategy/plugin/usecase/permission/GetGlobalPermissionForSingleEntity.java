package com.cs.config.strategy.plugin.usecase.permission;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.task.ITask;
import com.cs.core.config.interactor.exception.task.TaskNotFoundException;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionForSingleEntityModel;
import com.cs.core.config.interactor.model.globalpermissions.IGlobalPermissionWithAllowedModuleEntitiesModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class GetGlobalPermissionForSingleEntity extends AbstractOrientPlugin {
  
  public GetGlobalPermissionForSingleEntity(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetGlobalPermissionForSingleEntity/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String loginUserId = (String) requestMap.get(IGetGlobalPermissionForSingleEntityModel.USER_ID);
    String taskId = (String) requestMap.get(IGetGlobalPermissionForSingleEntityModel.ENTITY_ID);
    String entityType = (String) requestMap.get(IGetGlobalPermissionForSingleEntityModel.TYPE);
    Map<String, Object> globalPermissionToReturn = GlobalPermissionUtils
        .getDisabledGlobalPermission();
    Set<String> allowedEntities = new HashSet<>();
    Vertex roleConfiguredForUser = RoleUtils.getRoleFromUser(loginUserId);
    String roleConfiguredForUserId = UtilClass.getCodeNew(roleConfiguredForUser);
    List<String> roleEntities = (List<String>) (roleConfiguredForUser.getProperty(IRole.ENTITIES));
    allowedEntities.addAll(roleEntities);
    
    if (entityType.equals("task")) {
      try {
        Vertex taskNode = UtilClass.getVertexById(taskId, VertexLabelConstants.ENTITY_TYPE_TASK);
        if (taskNode.getProperty(ITask.TYPE)
            .equals(CommonConstants.TASK_TYPE_PERSONAL)) {
          globalPermissionToReturn = getGlobalPermissionForPersonalTask(loginUserId, taskId,
              entityType, roleConfiguredForUserId);
        }
        else {
          GlobalPermissionUtils.fillGlobalPermissionOfEntitiesForRole(taskId,
              globalPermissionToReturn, roleConfiguredForUserId);
        }
        List<String> roleIdsContainingLoginUser = (List<String>) requestMap
            .get(IGetGlobalPermissionForSingleEntityModel.ROLE_IDS_CONTAINING_LOGIN_USER);
        for (String roleIdContainingLoginUser : roleIdsContainingLoginUser) {
          if (roleConfiguredForUserId.equals(roleIdContainingLoginUser)) {
            continue;
          }
          Vertex role = UtilClass.getVertexById(roleIdContainingLoginUser,
              VertexLabelConstants.ENTITY_TYPE_ROLE);
          GlobalPermissionUtils.fillGlobalPermissionOfEntitiesForRole(taskId,
              globalPermissionToReturn, roleIdContainingLoginUser);
          
          roleEntities = (List<String>) (role.getProperty(IRole.ENTITIES));
          allowedEntities.addAll(roleEntities);
        }
      }
      catch (TaskNotFoundException e) {
        throw new TaskNotFoundException();
      }
    }
    else if (entityType.equals("klass")) {
      Set<String> klassIdsHavingReadPermission = GlobalPermissionUtils
          .getKlassIdsHavingReadPermission(roleConfiguredForUser);
      if (klassIdsHavingReadPermission.isEmpty()) {
        GlobalPermissionUtils.fillGlobalPermissionOfEntitiesForRole(taskId,
            globalPermissionToReturn, roleConfiguredForUserId);
      }
      else {
        if (klassIdsHavingReadPermission.contains(taskId)) {
          GlobalPermissionUtils.fillGlobalPermissionOfEntitiesForRole(taskId,
              globalPermissionToReturn, roleConfiguredForUserId);
        }
        else {
          Map<String, Object> permissionMap = GlobalPermissionUtils.getNoRoleGlobalPermission();
          GlobalPermissionUtils.mergeGlobalPermissons(globalPermissionToReturn, permissionMap);
        }
      }
    }
    else {
      GlobalPermissionUtils.fillGlobalPermissionOfEntitiesForRole(taskId, globalPermissionToReturn,
          roleConfiguredForUserId);
    }
    if (allowedEntities.isEmpty()) {
      allowedEntities.addAll(CommonConstants.MODULE_ENTITIES);
    }
    
    Map<String, Object> mapToReturn = new HashMap<>();
    mapToReturn.put(IGlobalPermissionWithAllowedModuleEntitiesModel.ALLOWED_ENTITIES,
        allowedEntities);
    mapToReturn.put(IGlobalPermissionWithAllowedModuleEntitiesModel.GLOBAL_PERMISSION,
        globalPermissionToReturn);
    
    return mapToReturn;
  }
  
  public static Map<String, Object> getGlobalPermissionForPersonalTask(String loginUserId,
      String taskId, String entityType, String roleId) throws Exception
  {
    Map<String, Object> globalPermissionToReturn = GlobalPermissionUtils
        .getDefaultPersonalTaskGlobalPermission();
    
    GlobalPermissionUtils.fillGlobalPermissionOfEntitiesForRole(taskId, globalPermissionToReturn,
        roleId);
    
    return globalPermissionToReturn;
  }
}
