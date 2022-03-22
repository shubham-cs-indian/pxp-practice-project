package com.cs.core.runtime.taskinstance;

import com.cs.constants.CommonConstants;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.globalpermissions.IGlobalPermissionWithAllowedModuleEntitiesModel;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.strategy.usecase.task.IGetConfigDetailsByTaskTypeStrategy;
import com.cs.core.config.strategy.usecase.task.IGetKlassInfoForLinkedContentStrategy;
import com.cs.core.config.strategy.usecase.user.IGetUserStrategy;
import com.cs.core.rdbms.task.idto.ITaskRecordDTO;
import com.cs.core.runtime.builder.TaskInstanceBuilder;
import com.cs.core.runtime.interactor.entity.taskinstance.ITaskRoleEntity;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveCreatePermission;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.taskinstance.*;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.utils.EntityUtil;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CreateTaskInstanceService
    extends AbstractRuntimeService<ITaskInstanceModel, IGetTaskInstanceModel>
    implements ICreateTaskInstanceService {
  
  @Autowired
  protected PermissionUtils                       permissionUtils;
  
  @Autowired
  protected IGetUserStrategy                      getUserStrategy;
  
  @Autowired
  protected IGetConfigDetailsByTaskTypeStrategy   getConfigDetailsByTaskTypeStrategy;
  
  @Autowired
  protected IGetKlassInfoForLinkedContentStrategy getKlassInfoForLinkedContentStrategy;
  
  @Autowired
  protected ISessionContext                       context;
  
  @Autowired
  protected TransactionThreadData                 transactionThread;
  
  @Autowired
  protected RDBMSComponentUtils                   rdbmsComponentUtils;
  
  @Override
  public IGetTaskInstanceModel executeInternal(ITaskInstanceModel dataModel) throws Exception
  {
    Boolean canCreate = permissionUtils.isUserHasPermissionToCreate(dataModel.getTypes().get(0), CommonConstants.TASK, null);
    if (!canCreate) {
      throw new UserNotHaveCreatePermission();
    }
    
    String type = dataModel.getTypes().get(0);
    IIdParameterModel idModel = new IdParameterModel(type);
    ICreateTaskInstanceConfigDetailsModel configDetails = getConfigDetailsByTaskTypeStrategy.execute(idModel);
   
    ITaskModel task = configDetails.getReferencedTask();
    
    if (task.getType().equals(CommonConstants.TASK_TYPE_SHARED) && dataModel.getIsCamundaCreated() == false) {
      IUserModel userModel = getUserStrategy.execute(new IdParameterModel(context.getUserId()));
      dataModel.getAccountable().getUserIds().add(userModel.getId());
      dataModel.getResponsible().getUserIds().add(userModel.getId());
      dataModel.getSignoff().getUserIds().add(userModel.getId());
    }
    //set data from transactionThread to eventInstance
    TransactionData transactionData = transactionThread.getTransactionData();
    
    dataModel.setPhysicalCatalogId(transactionData.getPhysicalCatalogId());
    dataModel.setOrganizationId(transactionData.getOrganizationId());
    dataModel.setEndpointId(EntityUtil.getDefaultIdIfNull(transactionData.getEndpointId()));
    dataModel.setLogicalCatalogId(EntityUtil.getDefaultIdIfNull(transactionData.getLogicalCatalogId()));
    dataModel.setSystemId(EntityUtil.getDefaultIdIfNull(transactionData.getSystemId()));
    
    ITaskRecordDTO taskRecordDTO = this.createTaskInstance(dataModel, configDetails);
    IGetTaskInstanceModel model = this.getTaskInstance(task, taskRecordDTO);
    
    IConfigTaskContentTypeResponseModel configTaskReferancesModel = getKlassInfoForLinkedContentStrategy
        .execute(prepareGetKlassInfoOfLinkedContentModel(model, taskRecordDTO));
    
    model.setconfigDetails(configTaskReferancesModel.getconfigDetails());
    
    List<String> roleIdsWithCurrentUser = permissionUtils.getRoleIdsWithCurrentUser();
    IGlobalPermissionWithAllowedModuleEntitiesModel globalPermissionForSingleEntity = permissionUtils
        .getGlobalPermissionForSingleEntity(model.getTypes().get(0), CommonConstants.TASK, roleIdsWithCurrentUser);
    model.setGlobalPermission(globalPermissionForSingleEntity.getGlobalPermission());
    return model;
  } 
  
  private IConfigTaskReferencesModel prepareGetKlassInfoOfLinkedContentModel(
      IGetTaskInstanceModel dataModel, ITaskRecordDTO taskRecordDTO) throws Exception
  {
    Map<String, List<String>> requestMap = new HashMap<>();
    List<String> userIds = new ArrayList<>();
    List<String> roleIds = new ArrayList<>();
    
    Map<String, String> userNameVsUserIdMap = rdbmsComponentUtils.getUserNameVsUserIdMap();
    
    taskRecordDTO.getUsersMap()
        .values()
        .forEach(users -> {
          users.forEach(userId -> {
            userIds.add(userNameVsUserIdMap.get(userId.getUserName()));
          });
        });
    
    taskRecordDTO.getRolesMap()
        .values()
        .forEach(roles -> {
          roles.forEach(role -> {
            roleIds.add(role);
          });
        });
    
    requestMap.put(ITaskRoleEntity.USERS_IDS, userIds);
    requestMap.put(ITaskRoleEntity.ROLES_IDS, roleIds);
    
    Map<String, ITaskReferencedInstanceModel> listOfRefInstances = dataModel
        .getReferencedInstances();
    IConfigTaskReferencesModel requestModel = new ConfigTaskReferencesModel();
    
    for (ITaskReferencedInstanceModel contentInstance : listOfRefInstances.values()) {
      requestMap.put(contentInstance.getId(), contentInstance.getTypes());
    }
    requestModel.setContentTypes(requestMap);
    
    return requestModel;
  }
  
  protected IGetTaskInstanceModel getTaskInstance(ITaskModel task, ITaskRecordDTO taskRecordDTO) throws Exception
  {
    return TaskInstanceBuilder.getTaskInstance(task, taskRecordDTO, this.rdbmsComponentUtils);
  }
  
  protected ITaskRecordDTO createTaskInstance(ITaskInstanceModel dataModel,
      ICreateTaskInstanceConfigDetailsModel configDetails) throws Exception
  {
    ITaskRecordDTO taskRecordDTO = TaskInstanceBuilder.createTaskInstance(dataModel, configDetails.getReferencedTask(), this.rdbmsComponentUtils, null);
    return taskRecordDTO;
  }
}
