package com.cs.config.strategy.plugin.usecase.permission;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.task.ITask;
import com.cs.core.config.interactor.exception.task.TaskNotFoundException;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionForSingleEntityModel;
import com.cs.core.config.interactor.model.globalpermissions.IGlobalPermissionWithAllowedModuleEntitiesModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class GetGlobalPermissionForSingleEntityForRole extends AbstractOrientPlugin {

  public GetGlobalPermissionForSingleEntityForRole(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetGlobalPermissionForSingleEntityForRole/*" };
  }

  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String roleId = (String) requestMap.get(IGetGlobalPermissionForSingleEntityModel.USER_ID);
    String taskId = (String) requestMap.get(IGetGlobalPermissionForSingleEntityModel.ENTITY_ID);
    String entityType = (String) requestMap.get(IGetGlobalPermissionForSingleEntityModel.TYPE);
    Map<String, Object> globalPermissionToReturn = GlobalPermissionUtils
        .getDisabledGlobalPermission();
    Set<String> allowedEntities = new HashSet<>();
    
    if (entityType.equals("task")) {
      try {
        Vertex taskNode = UtilClass.getVertexById(taskId, VertexLabelConstants.ENTITY_TYPE_TASK);
        if (taskNode.getProperty(ITask.TYPE).equals(CommonConstants.TASK_TYPE_PERSONAL)) {
          globalPermissionToReturn = getGlobalPermissionForPersonalTask(taskId, entityType, roleId);
        }
        else {
          GlobalPermissionUtils.fillGlobalPermissionOfEntitiesForRole(taskId, globalPermissionToReturn, roleId);
        }
      }
      catch (TaskNotFoundException e) {
        throw new TaskNotFoundException();
      }
    }
    
    Map<String, Object> mapToReturn = new HashMap<>();
    mapToReturn.put(IGlobalPermissionWithAllowedModuleEntitiesModel.ALLOWED_ENTITIES,
        allowedEntities);
    mapToReturn.put(IGlobalPermissionWithAllowedModuleEntitiesModel.GLOBAL_PERMISSION,
        globalPermissionToReturn);
    return mapToReturn;
  }
  
  public static Map<String, Object> getGlobalPermissionForPersonalTask(String taskId, String entityType, String roleId) throws Exception
  {
    Map<String, Object> globalPermissionToReturn = GlobalPermissionUtils.getDefaultPersonalTaskGlobalPermission();
    
    GlobalPermissionUtils.fillGlobalPermissionOfEntitiesForRole(taskId, globalPermissionToReturn, roleId);
    
    return globalPermissionToReturn;
  }
  
}
