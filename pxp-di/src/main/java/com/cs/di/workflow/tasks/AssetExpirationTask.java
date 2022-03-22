package com.cs.di.workflow.tasks;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.bgprocess.dto.AssetExpirationDTO;
import com.cs.core.bgprocess.idto.IAssetExpirationDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPPriority;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.executionstatus.IExecutionStatus;
import com.cs.di.workflow.model.executionstatus.IOutputExecutionStatusModel;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.workflow.base.TaskType;
import com.fasterxml.jackson.core.JsonProcessingException;

@Component("assetExpirationTask")
public class AssetExpirationTask extends AbstractBGPTask {
  
  private static final String      DATA_LANGUAGE       = "DATA_LANGUAGE";
  private static final String      PHYSICAL_CATALOG_ID = "PHYSICAL_CATALOG_ID";
  private static final String      ORGANIZATION_ID     = "ORGANIZATION_ID";
  private static final String      TIMESTAMP           = "TIMESTAMP";
  
  private static final List<String> INPUT_LIST  = Arrays.asList(SERVICE, SERVICE_DATA, PRIORITY, DATA_LANGUAGE, 
      PHYSICAL_CATALOG_ID, ORGANIZATION_ID, TIMESTAMP);
  
  private static final List<String> OUTPUT_LIST = Arrays.asList(JOB_ID, EXECUTION_STATUS);
  
  @Override
  public void executeTask(WorkflowTaskModel model)
  {
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = model.getExecutionStatusTable();
    try {
      model.getInputParameters().put(SERVICE, "ASSET_EXPIRATION");
      model.getInputParameters().put(PRIORITY, BGPPriority.LOW);
      //model.getInputParameters().put(USER_ID, model.getWorkflowModel().getTransactionData().getUserName());
      executeBGPService(model);
    }
    catch (CSFormatException | CSInitializationException | RDBMSException | JsonProcessingException e) {
      RDBMSLogger.instance().exception(e);
      executionStatusTable.addError(new ObjectCode[] { ObjectCode.MESSAGE_TYPE }, MessageCode.GEN500, new String[] {});
    }
  }
  
  @Override
  protected String prepareInputDTO(WorkflowTaskModel model) throws CSFormatException, RDBMSException
  {
   // TODO : replace hard coded values with values from TransactionData  
    IAssetExpirationDTO assetExpirationDTO = new AssetExpirationDTO();
    assetExpirationDTO.setTimeStamp(System.currentTimeMillis());
    assetExpirationDTO.setDataLanguage("en_US");
    assetExpirationDTO.setPhysicalCatalogId("pim");
    assetExpirationDTO.setOrganizationId("-1");
    return assetExpirationDTO.toJSON();
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
  public TaskType getTaskType()
  {
    return TaskType.SERVICE_TASK;
  }

  @Override
  public List<String> validate(Map<String, Object> inputFields)
  {
    return null;
  }
  
}
