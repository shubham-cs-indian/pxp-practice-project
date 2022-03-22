package com.cs.core.runtime.interactor.model.configuration;

import java.util.List;

public interface IExecutableTaskModel extends IModel {
  
  public static String TASK_LIST = "taskList";
  
  public List<IExecutableTaskInformationModel> getTaskList();
  
  public void setTaskList(List<IExecutableTaskInformationModel> taskList);
}
