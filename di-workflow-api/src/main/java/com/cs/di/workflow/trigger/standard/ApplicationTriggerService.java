package com.cs.di.workflow.trigger.standard;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;import com.cs.core.config.interactor.model.configdetails.IExportResponseModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.di.runtime.interactor.model.transfer.ITransferEntityRequestModel;
import com.cs.di.workflow.trigger.IWorkflowParameterModel;
import com.cs.workflow.base.EventType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ApplicationTriggerService extends WorkflowEventTriggerService<IApplicationTriggerModel>
    implements IApplicationTriggerService {
  
  private static final String BASE_ENTITY_IIDS            = "BASE_ENTITY_IIDS";
  private static final String DESTINATION_CATALOG_ID      = "DESTINATION_CATALOG_ID";
  private static final String DESTINATION_ORGANIZATION_ID = "DESTINATION_ORGANIZATION_ID";
  private static final String DESTINATION_ENDPOINT_ID     = "DESTINATION_ENDPOINT_ID";
  private static final String PARTNER_AUTHORIZATION_ID    = "PARTNER_AUTHORIZATION_ID";
  public static final String  IS_REVISIONABLE_TRANSFER    = "IS_REVISIONABLE_TRANSFER";
  
  @Override
  protected IListModel<IWorkflowParameterModel> getQualifyingWorkflows(
      IApplicationTriggerModel workflowEventModel) throws Exception
  {
    workflowEventModel.setEventType(EventType.APPLICATION.name());
    WorkflowEventTriggerModel filterModel = createBasicWorkflowFilter(workflowEventModel);
    return getProcessIds(filterModel);
  }
  
  @Override
  protected void addExecutionParameters(IApplicationTriggerModel workflowEventModel,
      Map<String, Object> workflowParameters)
  {
    // Handling for transfer
    if (IApplicationTriggerModel.ApplicationActionType.TRANSFER.name()
        .equals(workflowEventModel.getTriggeringType())) {
      ObjectMapper mapper = new ObjectMapper();
      Map<String, Object> serviceData = new HashMap<>();
      try {
        serviceData = mapper.readValue(workflowEventModel.getServiceData(),
            new TypeReference<HashMap<String, Object>>(){});
      }
      catch (Exception e) {
        e.printStackTrace();
        RDBMSLogger.instance().exception(e);
      }
      workflowParameters.put(BASE_ENTITY_IIDS, serviceData.get(ITransferEntityRequestModel.IDS));
      workflowParameters.put(DESTINATION_CATALOG_ID,
          serviceData.get(ITransferEntityRequestModel.DESTINATION_CATALOG_ID));
      workflowParameters.put(DESTINATION_ORGANIZATION_ID,
          serviceData.get(ITransferEntityRequestModel.DESTINATION_ORGANIZATION_ID));
      workflowParameters.put(DESTINATION_ENDPOINT_ID,
          serviceData.get(ITransferEntityRequestModel.DESTINATION_ENDPOINT_ID));
      workflowParameters.put(PARTNER_AUTHORIZATION_ID,
          serviceData.get(ITransferEntityRequestModel.AUTHORIZATION_MAPPING_ID));
      workflowParameters.put(IS_REVISIONABLE_TRANSFER, 
          serviceData.get(ITransferEntityRequestModel.IS_REVISIONABLE_TRANSFER));
      
    }
    else {
      Map<String, String> serviceDataMap = new HashMap<String, String>();
      serviceDataMap.put(SERVICE_DATA, workflowEventModel.getServiceData());
      workflowParameters.put(SERVICE_DATA, serviceDataMap);
      workflowParameters.put(SERVICE, workflowEventModel.getTriggeringType());
      workflowParameters.put(PRIORITY, workflowEventModel.getPriority());
    }
  }
}
