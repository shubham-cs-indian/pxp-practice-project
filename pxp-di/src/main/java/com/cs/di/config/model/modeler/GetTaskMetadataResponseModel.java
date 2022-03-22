package com.cs.di.config.model.modeler;

import java.util.List;

import com.cs.workflow.base.TaskType;

public class GetTaskMetadataResponseModel implements IGetTaskMetadataResponseModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    inputList;
  protected List<String>    outputList;
  protected TaskType        taskType;
   
  @Override
  public List<String> getInputList()
  {
    return inputList;
  }
  
  @Override
  public void setInputList(List<String> inputList)
  {
    this.inputList = inputList;
  }
  
  @Override
  public List<String> getOutputList()
  {
    return outputList;
  }
  
  @Override
  public void setOutputList(List<String> outputList)
  {
    this.outputList = outputList;
  }
  
  @Override
  public TaskType getTaskType()
  {
    return taskType;
  }

  @Override
  public void setTaskType(TaskType taskType)
  {
    this.taskType = taskType;
  }
}
