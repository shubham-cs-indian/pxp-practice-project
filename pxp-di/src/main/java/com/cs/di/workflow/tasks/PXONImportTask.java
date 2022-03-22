package com.cs.di.workflow.tasks;

import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.dataintegration.dto.PXONImporterPlanDTO;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.di.runtime.utils.DiValidationUtil;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.di.workflow.model.executionstatus.IExecutionStatus;
import com.cs.di.workflow.model.executionstatus.IOutputExecutionStatusModel;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.TaskType;
import com.cs.workflow.base.WorkflowType;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

@Component("PXONImportTask")
public class PXONImportTask extends AbstractBGPTask {
  
  private static final String            IMPORTED_PXON_ARCHIVE_PATH = "import.pxon.archive.path";
  private static final String            NFS_FILE_PATH              = "nfs.file.path";
  private static final String            FILE_NAME                  = "FILE_NAME";
  private static final String            PXON_IMPORT                = "PXON_IMPORT";
  public static final String             SUCCESS_IIDS               = "SUCCESS_IIDS";
  public static final String             FAILED_IIDS                = "FAILED_IIDS";
  
  public static final List<String>       INPUT_LIST                 = Arrays.asList(FILE_NAME);
  public static final List<String>       OUTPUT_LIST                = Arrays.asList(JOB_ID, SUCCESS_IIDS, FAILED_IIDS, EXECUTION_STATUS);
  public static final List<WorkflowType> WORKFLOW_TYPES             = Arrays.asList(WorkflowType.STANDARD_WORKFLOW);
  public static final List<EventType>    EVENT_TYPES                = Arrays.asList(EventType.BUSINESS_PROCESS);


  @Override
  public void executeTask(WorkflowTaskModel model)
  {
    String archieveFilePath = null;
    String bgpFilePath = null;
    
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = model.getExecutionStatusTable();
    String fileName = DiValidationUtil.validateAndGetRequiredString(model, FILE_NAME);
    if (executionStatusTable.isErrorOccurred()) {
      return;
    }
    try {
      archieveFilePath = CSProperties.instance().getString(IMPORTED_PXON_ARCHIVE_PATH);
      bgpFilePath = CSProperties.instance().getString(NFS_FILE_PATH) + "/";
      
      // Check if valid file name is provided or file exists in the destination
      File file = new File(bgpFilePath + fileName);
      if (file == null || !file.exists()) {
        executionStatusTable.addError(new ObjectCode[] { ObjectCode.MESSAGE_TYPE }, MessageCode.GEN024, new String[] { fileName });
        return;
      }
      
      PXONImporterPlanDTO importerPlanDTO = new PXONImporterPlanDTO();
      importerPlanDTO.setRelativeAfterImportFile(archieveFilePath + fileName);
      importerPlanDTO.setRelativeImportFile(fileName);
      importerPlanDTO.setCatalogCode(model.getWorkflowModel().getTransactionData().getPhysicalCatalogId());
      importerPlanDTO.setOrganizationCode(model.getWorkflowModel().getTransactionData().getOrganizationId());
      importerPlanDTO.setUserId(model.getWorkflowModel().getTransactionData().getUserId());
      importerPlanDTO.setUserName(model.getWorkflowModel().getTransactionData().getUserName());
      importerPlanDTO.setLocaleID(model.getWorkflowModel().getTransactionData().getDataLanguage());
      String partnerAuthorizationId = (String) model.getWorkflowModel().getWorkflowParameterMap().get(IEndpointModel.AUTHORIZATION_MAPPING);
      importerPlanDTO.setPartnerAuthorizationId(partnerAuthorizationId);
      Map<String, String> serviceDataMap = new HashMap<String, String>();
      serviceDataMap.put(SERVICE_DATA, importerPlanDTO.toJSON());
         
      model.getInputParameters().put(SERVICE, PXON_IMPORT);
      model.getInputParameters().put(PRIORITY, IBGProcessDTO.BGPPriority.LOW);
      model.getInputParameters().put(SERVICE_DATA, serviceDataMap);

      Long jobId = executeBGPService(model);
      
      executionStatusTable.addSuccess(new ObjectCode[] { ObjectCode.MESSAGE_TYPE }, MessageCode.GEN023,
          new String[] { fileName, String.valueOf(jobId) });
    }
    catch (CSInitializationException e) {
      executionStatusTable.addError(MessageCode.GEN027);
    }
    catch (CSFormatException | RDBMSException | JsonProcessingException e) {
      executionStatusTable.addError(new ObjectCode[] { ObjectCode.MESSAGE_TYPE }, MessageCode.GEN025, new String[] { fileName });
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
  
  /**
   * Validate input parameters
   * 
   * @param inputFields
   */
  @Override
  public List<String> validate(Map<String, Object> inputFields)
  {
    String filename = (String) inputFields.get(FILE_NAME);
    List<String> returnList = new ArrayList<>();
    if (filename==null || filename.isEmpty() || !isRuntimeValue(filename) && !filename.endsWith(".pxon")) {
      returnList.add(FILE_NAME);
    }
    // Note: Validation not required for :
    // JOB_ID, EXECUTION_STATUS as they are optional output parameters
    return returnList;
  }
}
