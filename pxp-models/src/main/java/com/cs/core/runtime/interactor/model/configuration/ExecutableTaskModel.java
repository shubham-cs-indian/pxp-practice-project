package com.cs.core.runtime.interactor.model.configuration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ExecutableTaskModel implements IExecutableTaskModel {
  
  protected List<IExecutableTaskInformationModel> taskList;
  
  @Override
  public List<IExecutableTaskInformationModel> getTaskList()
  {
    if (taskList == null) {
      taskList = new ArrayList<>();
    }
    return taskList;
  }
  
  @JsonDeserialize(contentAs = ExecutableTaskInformationModel.class)
  @Override
  public void setTaskList(List<IExecutableTaskInformationModel> taskList)
  {
    this.taskList = taskList;
  }
}
