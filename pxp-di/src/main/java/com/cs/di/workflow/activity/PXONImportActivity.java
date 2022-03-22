package com.cs.di.workflow.activity;

import org.springframework.stereotype.Component;

import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.di.workflow.tasks.PXONImportTask;
import com.cs.workflow.base.TaskType;

@Component("PXONImportActivity")
public class PXONImportActivity extends PXONImportTask {
	  
  @Override
  public TaskType getTaskType()
  {
    return TaskType.CALL_ACTIVIY_TASK;
  }
  
  @Override
  public void executeTask(WorkflowTaskModel model)
  {
    
  }

}
