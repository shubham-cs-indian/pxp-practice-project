package com.cs.core.runtime.interactor.model.taskinstance;

import com.cs.core.runtime.interactor.entity.taskinstance.ITaskInstance;
import com.cs.core.runtime.interactor.entity.taskinstance.TaskInstance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class TaskInstanceListModel implements ITaskInstanceListModel {
  
  private static final long     serialVersionUID = 1L;
  
  protected List<ITaskInstance> list             = new ArrayList<>();
  
  @Override
  public List<ITaskInstance> getList()
  {
    return list;
  }
  
  @JsonDeserialize(contentAs = TaskInstance.class)
  @Override
  public void setList(List<ITaskInstance> list)
  {
    this.list = list;
  }
}
