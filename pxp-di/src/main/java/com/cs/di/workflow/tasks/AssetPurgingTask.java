package com.cs.di.workflow.tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPPriority;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.di.workflow.model.executionstatus.IExecutionStatus;
import com.cs.di.workflow.model.executionstatus.IOutputExecutionStatusModel;
import com.cs.workflow.base.TaskType;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * This task is responsible for asset purging workflow.
 * @author santosh.kumar
 *
 */
@Component("assetPurgingTask")
public class AssetPurgingTask extends AbstractBGPTask {
  
  private static final List<String> OUTPUT_LIST = Arrays.asList(JOB_ID, EXECUTION_STATUS);
  
  @Override
  public void executeTask(WorkflowTaskModel model)
  {
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = model.getExecutionStatusTable();
    try {
      model.getInputParameters().put(SERVICE, "ASSET_PURGING");
      model.getInputParameters().put(PRIORITY, BGPPriority.LOW);
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
    return "{}";
  }
  
  @Override
  public List<String> getInputList()
  {
    return new ArrayList<String>();
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
