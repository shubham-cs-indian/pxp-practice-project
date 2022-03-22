package com.cs.core.runtime.taskinstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.constants.CommonConstants;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.model.globalpermissions.IGlobalPermissionWithAllowedModuleEntitiesModel;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.config.strategy.usecase.task.IGetKlassInfoForLinkedContentStrategy;
import com.cs.core.config.strategy.usecase.task.IGetTaskStrategy;
import com.cs.core.rdbms.task.idao.ITaskRecordDAO;
import com.cs.core.rdbms.task.idto.ITaskRecordDTO;
import com.cs.core.runtime.builder.TaskInstanceBuilder;
import com.cs.core.runtime.interactor.entity.taskinstance.ITaskRoleEntity;
import com.cs.core.runtime.interactor.exception.taskinstance.UserNotHaveReadPermissionForTask;
import com.cs.core.runtime.interactor.model.camunda.GetCamundaProcessDefinationModel;
import com.cs.core.runtime.interactor.model.camunda.ICamundaProcessModel;
import com.cs.core.runtime.interactor.model.camunda.IGetCamundaProcessDefinationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.taskinstance.ConfigTaskReferencesModel;
import com.cs.core.runtime.interactor.model.taskinstance.IConfigTaskContentTypeResponseModel;
import com.cs.core.runtime.interactor.model.taskinstance.IConfigTaskReferencesModel;
import com.cs.core.runtime.interactor.model.taskinstance.IGetTaskInstanceModel;
import com.cs.core.runtime.interactor.model.taskinstance.ITaskReferencedInstanceModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.cammunda.broadcast.IGetCamundaProcessDefinationStrategy;
import com.cs.core.runtime.strategy.utils.PermissionUtils;

@Service
public class GetTaskInstanceService extends AbstractRuntimeService<IIdParameterModel, IGetTaskInstanceModel>
    implements IGetTaskInstanceService {
  
  @Autowired
  protected IGetKlassInfoForLinkedContentStrategy getKlassInfoForLinkedContentStrategy;
  
  @Autowired
  protected PermissionUtils                       permissionUtils;
  
  @Autowired
  protected IGetTaskStrategy                      getTaskStrategy;
  
  @Autowired
  protected RDBMSComponentUtils                   rdbmsComponentUtils;
  
  @Autowired
  protected IGetCamundaProcessDefinationStrategy  getCamundaProcessDefinationStrategy;
  
  // @Autowired
  // protected TaskInstanceUtils taskInstanceUtils;
  
  @Override
  public IGetTaskInstanceModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    
    ITaskRecordDAO taskRecordDAO = this.rdbmsComponentUtils.openTaskDAO();
    ITaskRecordDTO taskRecordDTO = taskRecordDAO.getTaskByTaskIID(Long.parseLong(dataModel.getId()));
    
    ITaskModel taskConfig = this.getTaskInstance(taskRecordDTO.getTask().getCode());
    
    IGetTaskInstanceModel model = this.getTaskInstance(taskConfig, taskRecordDTO);
    
    IConfigTaskContentTypeResponseModel configTaskReferancesModel = getKlassInfoForLinkedContentStrategy
        .execute(prepareGetKlassInfoOfLinkedContentModel(model, taskRecordDTO));
    
    Map<String, ITaskReferencedInstanceModel> listOfRefInstances = model.getReferencedInstances();
    Map<String, List<String>> contentType = configTaskReferancesModel.getContentTypes();
    model.setconfigDetails(configTaskReferancesModel.getconfigDetails());
    for (String contentId : listOfRefInstances.keySet()) {
      ITaskReferencedInstanceModel linkedContent = listOfRefInstances.get(contentId);
      linkedContent.setTypes(contentType.get(contentId));
    }
    
    List<String> roleIdsWithCurrentUser = permissionUtils.getRoleIdsWithCurrentUser();
    
    IGlobalPermissionWithAllowedModuleEntitiesModel globalPermissionForSingleEntity = permissionUtils
        .getGlobalPermissionForSingleEntity(model.getTypes().get(0), CommonConstants.TASK, roleIdsWithCurrentUser);
    IGlobalPermission globalPermission = globalPermissionForSingleEntity.getGlobalPermission();
    if (!globalPermission.getCanRead() && !model.getIsPublic()) {
      throw new UserNotHaveReadPermissionForTask();
    }
    model.setGlobalPermission(globalPermission);
    
    setReferencedProcessDefinition(model);
    
    return model;
  }
  
  public void setReferencedProcessDefinition(IGetTaskInstanceModel model) throws Exception
  {
    if (model.getIsCamundaCreated()) {
      String camundaProcessDefinationId = model.getCamundaProcessDefinationId();
      
      IGetCamundaProcessDefinationModel getProcessDefinationModel = new GetCamundaProcessDefinationModel(camundaProcessDefinationId,
          model.getCamundaProcessInstanceId());
      
      ICamundaProcessModel response = getCamundaProcessDefinationStrategy.execute(getProcessDefinationModel);
      model.setReferencedProcessDefination(response);
    }
  }
  
  protected ITaskModel getTaskInstance(String takCode) throws Exception
  {
    IIdParameterModel iIdParameterModel = new IdParameterModel(takCode);
    return this.getTaskStrategy.execute(iIdParameterModel);
  }
  
  protected IGetTaskInstanceModel getTaskInstance(ITaskModel taskConfig, ITaskRecordDTO taskRecordDTO) throws Exception
  {
    return TaskInstanceBuilder.getTaskInstance(taskConfig, taskRecordDTO, this.rdbmsComponentUtils);
  }
  
  private IConfigTaskReferencesModel prepareGetKlassInfoOfLinkedContentModel(IGetTaskInstanceModel dataModel, ITaskRecordDTO taskRecordDTO)
      throws Exception
  {
    Map<String, List<String>> requestMap = new HashMap<>();
    List<String> userIds = new ArrayList<>();
    List<String> roleIds = new ArrayList<>();
    Map<String, String> userNameVsIds = rdbmsComponentUtils.getUserNameVsUserIdMap();
    taskRecordDTO.getUsersMap().values().forEach(users -> {
      users.forEach(userId -> {
        userIds.add(userNameVsIds.get(userId.getUserName()));
      });
    });
    
    taskRecordDTO.getRolesMap().values().forEach(roles -> {
      roles.forEach(role -> {
        roleIds.add(role);
      });
    });
    
    requestMap.put(ITaskRoleEntity.USERS_IDS, userIds);
    requestMap.put(ITaskRoleEntity.ROLES_IDS, roleIds);
    
    Map<String, ITaskReferencedInstanceModel> listOfRefInstances = dataModel.getReferencedInstances();
    IConfigTaskReferencesModel requestModel = new ConfigTaskReferencesModel();
    
    for (ITaskReferencedInstanceModel contentInstance : listOfRefInstances.values()) {
      requestMap.put(contentInstance.getId(), contentInstance.getTypes());
    }
    requestModel.setContentTypes(requestMap);
    
    return requestModel;
  }
  
}
