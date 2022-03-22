package com.cs.config.strategy.plugin.usecase.permission;

import com.cs.config.strategy.plugin.usecase.base.permission.AbstractGetGlobalPermissionForInstance;
import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.task.ITask;
import com.cs.core.config.interactor.exception.task.TaskNotFoundException;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionForMultipleEntityResponseModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionForMultipleInstancesRequestModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionRequestModel;
import com.cs.core.config.interactor.model.globalpermissions.IGlobalPermissionWithAllowedModuleEntitiesModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class GetGlobalPermissionForMultipleInstances
    extends AbstractGetGlobalPermissionForInstance {
  
  public GetGlobalPermissionForMultipleInstances(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetGlobalPermissionForMultipleInstances/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String loginUserId = (String) requestMap
        .get(IGetGlobalPermissionForMultipleInstancesRequestModel.USER_ID);
    List<Map<String, Object>> requestList = (List<Map<String, Object>>) requestMap
        .get(IGetGlobalPermissionForMultipleInstancesRequestModel.REQUEST_LIST);
    Map<String, Object> response = new HashMap<>();
    for (Map<String, Object> request : requestList) {
      String id = (String) request.get(IGetGlobalPermissionRequestModel.ID);
      String taskId = (String) request.get(IGetGlobalPermissionRequestModel.ENTITY_ID);
      
      Vertex taskNode = null;
      try {
        taskNode = UtilClass.getVertexById(taskId, VertexLabelConstants.ENTITY_TYPE_TASK);
      }
      catch (TaskNotFoundException e) {
        throw new TaskNotFoundException();
      }
      
      String type = taskNode.getProperty(ITask.TYPE);
      if (type.equals(CommonConstants.TASK_TYPE_PERSONAL)
          && request.get(IGetGlobalPermissionRequestModel.CREATED_BY)
              .equals(loginUserId)) {
        Map<String, Object> globalPermissionToReturn = GlobalPermissionUtils
            .getAllRightsGlobalPermission();
        Map<String, Object> tempMap = new HashMap<>();
        tempMap.put(IGlobalPermissionWithAllowedModuleEntitiesModel.ALLOWED_ENTITIES,
            new HashSet<>());
        tempMap.put(IGlobalPermissionWithAllowedModuleEntitiesModel.GLOBAL_PERMISSION,
            globalPermissionToReturn);
        response.put(id, tempMap);
      }
      else {
        List<String> roleIdsContainingLoginUser = (List<String>) request
            .get(IGetGlobalPermissionRequestModel.ROLE_IDS_CONTAINING_LOGIN_USER);
        
        Set<String> allowedEntities = new HashSet<>();
        Map<String, Object> globalPermissionToReturn = getGlobalPermissionForInstance(
            roleIdsContainingLoginUser, allowedEntities, taskId, loginUserId);
        
        Map<String, Object> tempMap = new HashMap<>();
        tempMap.put(IGlobalPermissionWithAllowedModuleEntitiesModel.ALLOWED_ENTITIES,
            allowedEntities);
        tempMap.put(IGlobalPermissionWithAllowedModuleEntitiesModel.GLOBAL_PERMISSION,
            globalPermissionToReturn);
        response.put(id, tempMap);
      }
    }
    Map<String, Object> mapToReturn = new HashMap<>();
    mapToReturn.put(IGetGlobalPermissionForMultipleEntityResponseModel.GLOBAL_PERMISSION, response);
    return mapToReturn;
  }
}
