package com.cs.di.runtime.interactor.initiateexport;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractService;
import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveExportPermission;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import com.cs.di.runtime.model.initiateexport.IExportDataModel;
import com.cs.di.workflow.trigger.standard.IIntegrationTriggerModel.IntegrationActionType;
import com.cs.di.workflow.trigger.standard.IIntegrationTriggerService;
import com.cs.di.workflow.trigger.standard.IntegrationTriggerModel;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class InitiateExportService extends AbstractService<IExportDataModel, IModel> implements IInitiateExportService {
  
  @Autowired
  protected IIntegrationTriggerService integrationEvent;
  
  @Autowired
  protected PermissionUtils       permissionUtils;

  @Override
  protected IModel executeInternal(IExportDataModel exportDataModel) throws Exception
  {
    checkExportPermission();

    IntegrationActionType actionType =     exportDataModel.getExportType().equals(IIntegrationTriggerService.ENTITY) ? IntegrationActionType.CONFIG_EXPORT : IntegrationActionType.EXPORT;
    // fetch export file path form properties file    
    Map<String, Object> parameters = new HashMap<>();
    parameters.put(IIntegrationTriggerService.SEARCH_CRITERIA, new ObjectMapper().writeValueAsString(exportDataModel.getSearchCriteria()));
    parameters.put(IIntegrationTriggerService.EXPORT_TYPE, exportDataModel.getExportType());
    parameters.put(IIntegrationTriggerService.CONFIG_PROPERTY_TYPE, exportDataModel.getConfigType());
    parameters.put(IIntegrationTriggerService.CONFIG_ENTITY_CODES, exportDataModel.getConfigCodes());
    parameters.put(IIntegrationTriggerService.LANGUAGE_CODE, exportDataModel.getLanguageCode());
    
    IntegrationTriggerModel integrationEventModel = new IntegrationTriggerModel();
    integrationEventModel.setWorkflowParameters(parameters);
    integrationEventModel.setIntegrationActionType(actionType);
    integrationEventModel.setEndpointId(exportDataModel.getEndpointId());
    integrationEvent.triggerQualifyingWorkflows(integrationEventModel);
    return new VoidModel();
  }

  @Override
  public ServiceType getServiceType()
  {
    return ServiceType.EXPORT;
  }

  private void checkExportPermission() throws Exception
  {
    IFunctionPermissionModel functionPermission = permissionUtils.getFunctionPermissionByUserId();
    if (!functionPermission.getCanExport()) {
      throw new UserNotHaveExportPermission();
    }
  }
}
