package com.cs.di.config.model.modeler;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.workflow.base.TaskType;

public interface IGetTaskMetadataResponseModel extends IModel {
  
  public static final String INPUT_LIST  = "inputList";
  public static final String OUTPUT_LIST = "outputList";
  public static final String TASK_TYPE   = "outputList";
  
  public List<String> getInputList();
  public void setInputList(List<String> inputList);
  
  public List<String> getOutputList();
  public void setOutputList(List<String> outputList);
  
  public TaskType getTaskType();
  public void setTaskType(TaskType taskType);
}
