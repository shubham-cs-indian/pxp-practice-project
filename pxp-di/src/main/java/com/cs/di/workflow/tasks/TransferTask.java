package com.cs.di.workflow.tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.bgprocess.dto.TransferPlanDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.ITransferPlanDTO;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.di.runtime.utils.DiValidationUtil;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.di.workflow.model.executionstatus.IExecutionStatus;
import com.cs.di.workflow.model.executionstatus.IOutputExecutionStatusModel;
import com.cs.di.workflow.trigger.standard.IApplicationTriggerModel;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.TaskType;
import com.cs.workflow.base.WorkflowType;

@Component("transferTask")
public class TransferTask extends AbstractBGPTask {
  
  @Autowired
  protected ISessionContext                                context;
  
  private static final String            BASE_ENTITY_IIDS            = "BASE_ENTITY_IIDS";
  private static final String            DESTINATION_CATALOG_ID      = "DESTINATION_CATALOG_ID";
  private static final String            DESTINATION_ORGANIZATION_ID = "DESTINATION_ORGANIZATION_ID";
  private static final String            DESTINATION_ENDPOINT_ID     = "DESTINATION_ENDPOINT_ID";
  private static final String            PARTNER_AUTHORIZATION_ID    = "PARTNER_AUTHORIZATION_ID";
  public static final String             SUCCESS_IIDS                = "SUCCESS_IIDS";
  public static final String             FAILED_IIDS                 = "FAILED_IIDS";
  public static final String             IS_REVISIONABLE_TRANSFER    = "IS_REVISIONABLE_TRANSFER";
  
  public static final List<String>       INPUT_LIST                  = Arrays.asList(
      BASE_ENTITY_IIDS, DESTINATION_CATALOG_ID, DESTINATION_ORGANIZATION_ID,
      DESTINATION_ENDPOINT_ID, PARTNER_AUTHORIZATION_ID, IS_REVISIONABLE_TRANSFER);
  public static final List<String>       OUTPUT_LIST                 = Arrays.asList(SUCCESS_IIDS,
      FAILED_IIDS, JOB_ID, EXECUTION_STATUS);
  public static final List<WorkflowType> WORKFLOW_TYPES              = Arrays
      .asList(WorkflowType.STANDARD_WORKFLOW);
  public static final List<EventType>    EVENT_TYPES                 = Arrays
      .asList(EventType.BUSINESS_PROCESS);
  
  @SuppressWarnings("unchecked")
  @Override
  public void executeTask(WorkflowTaskModel model)
  {
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = model
        .getExecutionStatusTable();
    try {
      ITransactionData transactionData = model.getWorkflowModel().getTransactionData();
       Collection<String> getBaseEntityIIDs = (Collection<String>) DiValidationUtil
          .validateAndGetRequiredCollection(model, BASE_ENTITY_IIDS);
       List<Long> baseEntityIIDs = getBaseEntityIIDs.stream()
          .map(Long::parseLong)
          .collect(Collectors.toList());
      String destinationCatalogId = DiValidationUtil.validateAndGetRequiredString(model,
          DESTINATION_CATALOG_ID);
      String destinationOrganizationId = DiValidationUtil.validateAndGetOptionalString(model,
          DESTINATION_ORGANIZATION_ID);
      String destinationEndpointId = DiValidationUtil.validateAndGetOptionalString(model,
          DESTINATION_ENDPOINT_ID);
      String partnerAuthorizationID = DiValidationUtil.validateAndGetOptionalString(model,
          PARTNER_AUTHORIZATION_ID);
      Boolean isRevisionableTransfer = DiValidationUtil.validateAndGetRequiredBoolean(model,
          IS_REVISIONABLE_TRANSFER);
      
      if(context.getUserId() == null)
        context.setUserId(transactionData.getUserId());
      // Prepare transfer plan dto
      ITransferPlanDTO transferDTO = new TransferPlanDTO();
      transferDTO.setBaseEntityIIDs(baseEntityIIDs);
      transferDTO.setAllProperties(true);
      transferDTO.setSourceCatalogCode(transactionData.getPhysicalCatalogId());
      transferDTO.setSourceEndPointCode(transactionData.getEndpointId());
      transferDTO.setSourceOrganizationCode(transactionData.getOrganizationId());
      if (DiValidationUtil.isBlank(destinationOrganizationId)) {
        transferDTO.setTargetOrganizationCode(transactionData.getOrganizationId());
      }
      else {
        transferDTO.setTargetOrganizationCode(destinationOrganizationId);
      }
      transferDTO.setTargetCatalogCode(destinationCatalogId);
      transferDTO.setTargetEndPointCode(destinationEndpointId);
      transferDTO.setUserId(context.getUserId()!=null?context.getUserId():transactionData.getUserId());
      transferDTO.setLocaleID(transactionData.getDataLanguage());
      transferDTO.setAuthorizationMappingId(partnerAuthorizationID);
      transferDTO.setIsRevisionableTransfer(isRevisionableTransfer);
      
      Map<String, String> serviceDataMap = new HashMap<String, String>();
      serviceDataMap.put(SERVICE_DATA, transferDTO.toJSON());
      // Add input parameters
      model.getInputParameters().put(SERVICE, IApplicationTriggerModel.ApplicationActionType.TRANSFER.name());
      model.getInputParameters().put(PRIORITY, IBGProcessDTO.BGPPriority.MEDIUM);
      model.getInputParameters().put(SERVICE_DATA, serviceDataMap);
      
      // execute bgp
      Long jobId = executeBGPService(model);
      
      executionStatusTable.addSuccess(new ObjectCode[] { ObjectCode.MESSAGE_TYPE },
          MessageCode.GEN044, new String[] { String.valueOf(jobId) });
    }
    catch (Exception e) {
      executionStatusTable.addError(MessageCode.GEN043);
    }
  }
  
  @Override
  public List<String> getInputList()
  {
    return INPUT_LIST;
  }
  
  @Override
  public List<String> getOutputList()
  {
    return OUTPUT_LIST;
  }
  
  @Override
  public List<WorkflowType> getWorkflowTypes()
  {
    return WORKFLOW_TYPES;
  }
  
  @Override
  public List<EventType> getEventTypes()
  {
    return EVENT_TYPES;
  }
  
  @Override
  public TaskType getTaskType()
  {
    return TaskType.BGP_TASK;
  }
  
  @Override
  public List<String> validate(Map<String, Object> inputFields)
  {
    List<String> returnList = new ArrayList<>();
    String baseEntityIID = (String) inputFields.get(BASE_ENTITY_IIDS);
    if (DiValidationUtil.isBlank(baseEntityIID) || !isRuntimeValue(baseEntityIID)) {
      returnList.add(BASE_ENTITY_IIDS);
    }
    
    String catalogId = (String) inputFields.get(DESTINATION_CATALOG_ID);
    if (DiValidationUtil.isBlank(catalogId)) {
      returnList.add(DESTINATION_CATALOG_ID);
    }
    else {
      String endpointId = (String) inputFields.get(DESTINATION_ENDPOINT_ID);
      if (catalogId.equals("dataIntegration") && DiValidationUtil.isBlank(endpointId)) {
        returnList.add(DESTINATION_ENDPOINT_ID);
      }
    }
    // Note: Validation not required for :
    // DESTINATION_ORGANIZATION_ID, DESTINATION_ENDPOINT_ID,
    // PARTNER_AUTHORIZATION_ID as they are optional input parameters
    return returnList;
  }
  
}
