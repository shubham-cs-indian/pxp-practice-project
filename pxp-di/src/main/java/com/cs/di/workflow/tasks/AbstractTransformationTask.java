package com.cs.di.workflow.tasks;

import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.model.WorkflowTaskModel;

/**
 * Process input file and generate PXON
 * 
 * @author ruplesh.pawar
 *
 */
public abstract class AbstractTransformationTask extends AbstractTask implements ITransformationTask {
  
  public static final String EXPORT_FILE_PREFIX  = "Export_";
  public static final String EXPORTED_TYPE = "EXPORTED_TYPE";
  
  /**
   * Method Transform PXON into BaseEntityDTO
   * and pass it further by setting model
   * @param model
   */
  public abstract void transform(WorkflowTaskModel model) throws Exception;

  /**
   * Executes transformation task.
   */
  @Override
  public void executeTask(WorkflowTaskModel model)
  {
    try {
      transform(model);
    }
    catch (Exception e) {
      model.getExecutionStatusTable().addError(MessageCode.GEN010);
    }
  }
}
