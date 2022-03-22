package com.cs.core.runtime.interactor.model.taskinstance;

import com.cs.core.runtime.interactor.entity.taskinstance.ITaskInstance;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ITaskInstanceListModel extends IModel {
  
  public static final String LIST = "list";
  
  public List<ITaskInstance> getList();
  
  public void setList(List<ITaskInstance> list);
}
