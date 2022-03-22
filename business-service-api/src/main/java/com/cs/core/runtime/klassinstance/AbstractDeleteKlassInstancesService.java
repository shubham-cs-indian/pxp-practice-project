package com.cs.core.runtime.klassinstance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.model.globalpermissions.GetGlobalPermissionForMultipleNatureTypesRequestModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionForMultipleNatureTypesRequestModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionForMultipleNatureTypesResponseModel;
import com.cs.core.config.strategy.usecase.globalpermissions.IGetGlobalPermissionForMultipleNatureTypesStrategy;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.runtime.interactor.exception.camunda.TasksLinkedToArticleIsInProgressException;
import com.cs.core.runtime.interactor.model.klassinstance.DeleteInstanceDetails;
import com.cs.core.runtime.interactor.model.klassinstance.DeleteInstancesResponseModel;
import com.cs.core.runtime.interactor.model.klassinstance.DeleteKlassInstancesRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteInstanceDetails;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteInstancesResponseModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteKlassInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteKlassInstanceRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteKlassInstanceResponseModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteKlassInstancesRequestModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.usecase.klassinstance.IDeleteKlassInstances;
import com.cs.workflow.camunda.IBPMNEngineService;

public abstract class AbstractDeleteKlassInstancesService<P extends IDeleteKlassInstanceRequestModel, R extends IDeleteKlassInstanceResponseModel>
    extends AbstractRuntimeService<P, R> {
  
  @Autowired
  protected RDBMSComponentUtils                                rdbmsComponentUtils;
  
  @Autowired
  protected IGetGlobalPermissionForMultipleNatureTypesStrategy getGlobalPermissionForMultipleNatureTypesStrategy;
  
  @Autowired
  protected IDeleteKlassInstances                              deleteKlassInstances;
  
  @Autowired
  protected IBPMNEngineService                                 camundaServiceManager;
  
  @Override
  @SuppressWarnings("unchecked")
  protected R executeInternal(P deleteModel) throws Exception
  {
    List<IDeleteKlassInstanceModel> deleteRequest = deleteModel.getDeleteRequest();
    Boolean isDeleteFromArchivalPortal = deleteModel.getIsDeleteFromArchivalPortal();
    Set<String> classifierCodes = new HashSet<>();
    List<IDeleteInstanceDetails> allDeleteInstanceDetails = new ArrayList<>();
    ExceptionModel failure = new ExceptionModel();
    
    List<String> idsNotToDelete = getIdsNotToDelete(deleteRequest);
    for (IDeleteKlassInstanceModel deleteKlassInstanceModel : deleteRequest) {
      List<String> ids = deleteKlassInstanceModel.getIds();
      String baseType = deleteKlassInstanceModel.getBaseType();
      for (String id : ids) {
        if (idsNotToDelete.contains(id)) {
          ExceptionUtil.addFailureDetailsToFailureObject(failure, new TasksLinkedToArticleIsInProgressException(), id, null);
          continue;
        }
        IDeleteInstanceDetails deleteInstanceDetails = new DeleteInstanceDetails();
        long baseEntityIID = Long.parseLong(id);
        if(!isDeleteFromArchivalPortal) {
          IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(baseEntityIID);
          String classifierCode = (String) baseEntityDAO.getBaseEntityDTO().getNatureClassifier().getClassifierCode();
          deleteInstanceDetails.setClassifierCode(classifierCode);
          deleteInstanceDetails.setBaseType(baseType);
          classifierCodes.add(classifierCode);
        }
        deleteInstanceDetails.setId(id);
        allDeleteInstanceDetails.add(deleteInstanceDetails);
      }
    }
    if (allDeleteInstanceDetails.isEmpty()) {
      IDeleteInstancesResponseModel deleteResponseModel = new DeleteInstancesResponseModel();
      deleteResponseModel.setFailure(failure);
      return (R) deleteResponseModel;
    }
    Map<String, IGlobalPermission> globalPermissions = getGlobalPermission(classifierCodes);
    
    IDeleteKlassInstancesRequestModel deleteKlassInstancesRequestModel = new DeleteKlassInstancesRequestModel();
    deleteKlassInstancesRequestModel.setHasDeletePermission(deleteModel.getHasDeletePermission());
    deleteKlassInstancesRequestModel.setGlobalPermissions(globalPermissions);
    deleteKlassInstancesRequestModel.setAllDeleteInstanceDetails(allDeleteInstanceDetails);
    deleteKlassInstancesRequestModel.setIsDeleteFromArchive(isDeleteFromArchivalPortal);
    IDeleteInstancesResponseModel deleteResponseModel = deleteKlassInstances.execute(deleteKlassInstancesRequestModel);
    if (!deleteResponseModel.getFailure().getDevExceptionDetails().isEmpty()
        || !deleteResponseModel.getFailure().getExceptionDetails().isEmpty()) {
      failure.getExceptionDetails().addAll(deleteResponseModel.getFailure().getExceptionDetails());
      failure.getDevExceptionDetails().addAll(deleteResponseModel.getFailure().getDevExceptionDetails());
    }
    
    deleteResponseModel.setFailure(failure);
    return (R) deleteResponseModel;
  }
  
  /**
   * Get KlassInstanceIds For Running Processes
   * 
   * @param deleteRequest
   * @return
   */
  private List<String> getIdsNotToDelete(List<IDeleteKlassInstanceModel> deleteRequest)
  {
    
    List<String> iids = new ArrayList<>();
    deleteRequest.forEach(instanceInformation -> {
      iids.addAll(instanceInformation.getIds());
    });
    List<String> idsNotToDelete = camundaServiceManager.getKlassInstanceIdsForRunningProcesses(iids);
    return idsNotToDelete;
  }
  
  protected Map<String, IGlobalPermission> getGlobalPermission(Set<String> classifierCodes) throws Exception
  {
    IGetGlobalPermissionForMultipleNatureTypesRequestModel globalPermissionRequestModel = new GetGlobalPermissionForMultipleNatureTypesRequestModel();
    globalPermissionRequestModel.setUserId(context.getUserId());
    globalPermissionRequestModel.setKlassIds(classifierCodes);
    
    IGetGlobalPermissionForMultipleNatureTypesResponseModel globalPermissionResponseModel = getGlobalPermissionForMultipleNatureTypesStrategy
        .execute(globalPermissionRequestModel);
    
    return globalPermissionResponseModel.getGlobalPermission();
  }
  
}
