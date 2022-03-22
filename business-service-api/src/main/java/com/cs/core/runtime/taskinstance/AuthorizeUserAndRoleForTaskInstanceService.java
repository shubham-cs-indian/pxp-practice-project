package com.cs.core.runtime.taskinstance;

import com.cs.constants.CommonConstants;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.globalpermissions.IGlobalPermissionWithAllowedModuleEntitiesModel;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.config.strategy.usecase.task.IGetTaskStrategy;
import com.cs.core.rdbms.task.idao.ITaskRecordDAO;
import com.cs.core.rdbms.task.idto.ITaskRecordDTO;
import com.cs.core.runtime.builder.TaskInstanceBuilder;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.taskinstance.*;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.cammunda.broadcast.IGetCamundaProcessDefinationStrategy;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AuthorizeUserAndRoleForTaskInstanceService extends AbstractRuntimeService<ISaveTaskInstanceModel, IPermissionsForEntityStatusModel>
implements IAuthorizeUserAndRoleForTaskInstanceService {

  
  @Autowired
  protected RDBMSComponentUtils       rdbmsComponentUtils;
  
  @Autowired
  protected PermissionUtils  permissionUtils;
  
  @Autowired
  protected IGetTaskStrategy getTaskStrategy;
  
  @Autowired
  protected IGetCamundaProcessDefinationStrategy     getCamundaProcessDefinationStrategy;
  
  
  @Override
  public IPermissionsForEntityStatusModel executeInternal(ISaveTaskInstanceModel dataModel) throws Exception
  {
    
     
    ITaskRecordDAO taskDAO = this.rdbmsComponentUtils.openTaskDAO();
    
    ITaskRecordDTO taskRecordDTO = taskDAO.getTaskByTaskIID(Long.valueOf(dataModel.getId()));
    IPermissionsForEntityStatusModel responsemodel= new PermissionsForEntityStatusModel();
    ITaskModel taskConfig = this.getTaskInstance(taskRecordDTO.getTask().getCode());
    List<String> unAuthorizedUsersOrRoles = getUnAuthorizedUserForEntity( dataModel,  taskConfig,taskRecordDTO);
    if(!unAuthorizedUsersOrRoles.isEmpty()|| !(unAuthorizedUsersOrRoles.size() > 0)) {
      responsemodel.setUnAuthorizedUsersOrRoles(unAuthorizedUsersOrRoles);
    }
    return responsemodel;
  }
  
  private  List<String> getUnAuthorizedUserForEntity(ISaveTaskInstanceModel dataModel,  ITaskModel taskConfig ,ITaskRecordDTO taskRecordDTO) throws Exception
  {  
   IGetTaskInstanceModel model = TaskInstanceBuilder.getTaskInstance(taskConfig,  taskRecordDTO, this.rdbmsComponentUtils);
    List<String> unAuthorizedUsers = new ArrayList<String>();
    List<String> unAuthorizedRoles = new ArrayList<String>();
    List<String> unAuthorizedUsersAndRoles = new ArrayList<String>();
    checkReadAndUpdateAuthorizationForEntity(model, unAuthorizedUsers,unAuthorizedRoles,  dataModel.getResponsible());
    checkCreateReadUpdateAndDeleteAuthorizationForEntity(model,unAuthorizedUsers,unAuthorizedRoles, dataModel.getAccountable());
    checkReadAndUpdateAuthorizationForEntity(model, unAuthorizedUsers,unAuthorizedRoles, dataModel.getVerify());
    checkReadAuthorizationForEntity(model, unAuthorizedUsers,unAuthorizedRoles, dataModel.getConsulted());
    checkReadAuthorizationForEntity(model, unAuthorizedUsers,unAuthorizedRoles, dataModel.getInformed());
    checkReadAndUpdateAuthorizationForEntity(model, unAuthorizedUsers,unAuthorizedRoles,  dataModel.getSignoff());
     getUserListByUserId(unAuthorizedUsers,unAuthorizedUsersAndRoles);
     getRoleListByRoleId(unAuthorizedRoles,unAuthorizedUsersAndRoles);
    return unAuthorizedUsersAndRoles;
 
  }

  private void getRoleListByRoleId(List<String> listOfRolesWithoutPermission,
      List<String> responseList) throws Exception
  {
    IListModel<IConfigEntityInformationModel> rolesModel = permissionUtils
        .getRolesByIds(listOfRolesWithoutPermission);
    List<? extends IConfigEntityInformationModel> roleList = (List<? extends IConfigEntityInformationModel>) rolesModel
        .getList();
    
    for (IConfigEntityInformationModel role : roleList) {
      responseList.add(role.getLabel());
    }
       
  }

  private void getUserListByUserId(List<String> listOfUsersWithoutPermission, List<String> responseList) throws Exception
  {
    Map<String, String> userNameVsId = rdbmsComponentUtils.getUserNameVsUserIdMap(); 
    for(String userId : listOfUsersWithoutPermission) {
      String userIdlable = userNameVsId.entrySet().stream()
      .filter(e -> e.getValue().equals(userId))
      .map(Map.Entry::getKey)
      .findFirst()
      .orElse(null);
      if(userIdlable != null) {
        responseList.add(userIdlable);
      }
    }
    
  }

  private void checkReadAndUpdateAuthorizationForEntity(IGetTaskInstanceModel model, List<String> unAuthorizedUsers,
      List<String> unAuthorizedRoles, ITaskRoleSaveEntity tskRoleSaveEntity) throws Exception
  {
    List<String> listOfRoleIds = tskRoleSaveEntity.getAddedRoleIds();
    
    List<String> listOfUserIds = tskRoleSaveEntity.getAddedUserIds();
    for(String userId : listOfUserIds ) {
   
        IGlobalPermissionWithAllowedModuleEntitiesModel globalPermissionForSingleEntity =  permissionUtils.getGlobalPermissionForSingleEntityForUser(model.getTypes()
            .get(0), CommonConstants.TASK,userId);
        if(!globalPermissionForSingleEntity.getGlobalPermission().getCanEdit() || !globalPermissionForSingleEntity.getGlobalPermission().getCanRead()) {
          unAuthorizedUsers.add(userId);
        }
      }
    for(String roleId : listOfRoleIds ) {
      
      IGlobalPermissionWithAllowedModuleEntitiesModel globalPermissionForSingleEntity =  permissionUtils.getGlobalPermissionForSingleEntityForRole(model.getTypes()
          .get(0), CommonConstants.TASK,roleId);
      if(!globalPermissionForSingleEntity.getGlobalPermission().getCanEdit() || !globalPermissionForSingleEntity.getGlobalPermission().getCanRead()) {
        unAuthorizedRoles.add(roleId);
      }
      }
  }
  
  private void checkCreateReadUpdateAndDeleteAuthorizationForEntity(IGetTaskInstanceModel model, List<String> unAuthorizedUsers,
      List<String> unAuthorizedRoles, ITaskRoleSaveEntity tskRoleSaveEntity) throws Exception
  {
    List<String> listOfRoleIds = tskRoleSaveEntity.getAddedRoleIds();
    
    List<String> listOfUserIds = tskRoleSaveEntity.getAddedUserIds();
    for(String userId : listOfUserIds ) {
   
        IGlobalPermissionWithAllowedModuleEntitiesModel globalPermissionForSingleEntity =  permissionUtils.getGlobalPermissionForSingleEntityForUser(model.getTypes()
            .get(0), CommonConstants.TASK,userId);
        if(!globalPermissionForSingleEntity.getGlobalPermission().getCanEdit() || !globalPermissionForSingleEntity.getGlobalPermission().getCanCreate()
            || !globalPermissionForSingleEntity.getGlobalPermission().getCanDelete() || !globalPermissionForSingleEntity.getGlobalPermission().getCanRead()) {
          unAuthorizedUsers.add(userId);
        }
      }
    for(String roleId : listOfRoleIds ) {
      
      IGlobalPermissionWithAllowedModuleEntitiesModel globalPermissionForSingleEntity =  permissionUtils.getGlobalPermissionForSingleEntityForRole(model.getTypes()
          .get(0), CommonConstants.TASK,roleId);
      if(!globalPermissionForSingleEntity.getGlobalPermission().getCanEdit() || !globalPermissionForSingleEntity.getGlobalPermission().getCanCreate()
          || !globalPermissionForSingleEntity.getGlobalPermission().getCanDelete() || !globalPermissionForSingleEntity.getGlobalPermission().getCanRead()) {
        unAuthorizedRoles.add(roleId);
      }
      }
  }
  
  private void checkReadAuthorizationForEntity(IGetTaskInstanceModel model, List<String> unAuthorizedUsers,
      List<String> unAuthorizedRoles, ITaskRoleSaveEntity tskRoleSaveEntity) throws Exception
  {
    List<String> listOfRoleIds = tskRoleSaveEntity.getAddedRoleIds();
    
    List<String> listOfUserIds = tskRoleSaveEntity.getAddedUserIds();
    for(String userId : listOfUserIds ) {
   
        IGlobalPermissionWithAllowedModuleEntitiesModel globalPermissionForSingleEntity =  permissionUtils.getGlobalPermissionForSingleEntityForUser(model.getTypes()
            .get(0), CommonConstants.TASK,userId);
        if(!globalPermissionForSingleEntity.getGlobalPermission().getCanRead()) {
          unAuthorizedUsers.add(userId);
        }
      }
    for(String roleId : listOfRoleIds ) {
      
      IGlobalPermissionWithAllowedModuleEntitiesModel globalPermissionForSingleEntity =  permissionUtils.getGlobalPermissionForSingleEntityForRole(model.getTypes()
          .get(0), CommonConstants.TASK,roleId);
      if(!globalPermissionForSingleEntity.getGlobalPermission().getCanRead()) {
        unAuthorizedRoles.add(roleId);
      }
      }
  }

  protected ITaskModel getTaskInstance(String takCode) throws Exception
  {
    IIdParameterModel iIdParameterModel = new IdParameterModel(takCode);
    return this.getTaskStrategy.execute(iIdParameterModel);
  }
  
  
  
}
