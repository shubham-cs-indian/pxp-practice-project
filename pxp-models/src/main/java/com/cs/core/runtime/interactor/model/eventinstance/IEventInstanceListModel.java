package com.cs.core.runtime.interactor.model.eventinstance;

import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstance;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IEventInstanceListModel extends IModel {
  
  public static final String LIST = "list";
  
  public List<IEventInstance> getList();
  
  public void setList(List<IEventInstance> list);
}
