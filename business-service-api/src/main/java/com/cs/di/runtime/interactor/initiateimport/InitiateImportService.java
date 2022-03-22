package com.cs.di.runtime.interactor.initiateimport;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractService;
import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveImportPermission;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import com.cs.di.runtime.model.initiateimport.IImportDataModel;
import com.cs.di.runtime.utils.DiFileUtils;
import com.cs.di.workflow.trigger.standard.IIntegrationTriggerModel.IntegrationActionType;
import com.cs.di.workflow.trigger.standard.IIntegrationTriggerService;
import com.cs.di.workflow.trigger.standard.IntegrationTriggerModel;

@Service
public class InitiateImportService extends AbstractService<IImportDataModel, IVoidModel> implements IInitiateImportService {
  
  @Autowired
  protected IIntegrationTriggerService integrationEvent;
  
  @Autowired
  protected PermissionUtils       permissionUtils;

  @Override
  protected IVoidModel executeInternal(IImportDataModel model) throws Exception
  {
    checkImportPermission();

    String originalfilename = model.getOriginalFilename();
    String baseFileName = FilenameUtils.getBaseName(originalfilename) + getLocalDateAndTimeAsString();
    originalfilename = baseFileName + "." + FilenameUtils.getExtension(originalfilename);
    Map<String, String> excelDataMap = new HashMap<>();
    excelDataMap.put(originalfilename, Base64.getEncoder().encodeToString(model.getFileData()));
    Map<String, Object> parameters = new HashMap<>();
    parameters.put(IIntegrationTriggerService.RECEIVED_DATA, excelDataMap);
    IntegrationActionType actionType;
    if (PRODUCT.equals(model.getImportType())) {
      actionType = IntegrationActionType.IMPORT;
      TransactionData transactionData = transactionThread.getTransactionData();
      if (model.getEndpointId() != null || transactionData.getEndpointId() != null) {
        // dump file in _DI_PROCESSING folder
        String endpointId = transactionData.getEndpointId() == null || transactionData.getEndpointId().isEmpty() ? model.getEndpointId()
            : transactionData.getEndpointId();
        String OrganizationId = transactionData.getOrganizationId();
        String endpointFolderPath = DiFileUtils.getImportedFilesFolderName(endpointId, OrganizationId);
        
        File endpoint = new File(endpointFolderPath);
        if (!endpoint.exists()) {
          endpoint.mkdir();
        }
        File serverFile = new File(endpointFolderPath + File.separator + baseFileName);
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
        stream.write(model.getFileData());
        stream.close();
      }
    } else {
      parameters.put(IIntegrationTriggerService.NODE_TYPE, model.getentitytype());
      parameters.put(IIntegrationTriggerService.PERMISSION_TYPES, model.getPermissionTypes());
      parameters.put(IIntegrationTriggerService.ROLE_IDS, model.getRoleIds());
      
      actionType = IntegrationActionType.CONFIG_IMPORT;
    }
    IntegrationTriggerModel integrationEventModel = new IntegrationTriggerModel();
    integrationEventModel.setWorkflowParameters(parameters);
    integrationEventModel.setIntegrationActionType(actionType);
    integrationEventModel.setEndpointId(model.getEndpointId());
    integrationEvent.triggerQualifyingWorkflows(integrationEventModel);
    return new VoidModel();
  }
  
  private String getLocalDateAndTimeAsString()
  {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    LocalDateTime now = LocalDateTime.now();
    return dtf.format(now);
  }
  
  @Override
  public ServiceType getServiceType()
  {
    return ServiceType.IMPORT;
  }

  private void checkImportPermission() throws Exception
  {
    IFunctionPermissionModel functionPermission = permissionUtils.getFunctionPermissionByUserId();
    if (!functionPermission.getCanImport()) {
      throw new UserNotHaveImportPermission();
    }
  }
}
