package com.cs.di.config.interactor.model.initializeworflowevent;

import com.cs.core.config.interactor.model.processevent.IProcessEventModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetAllProcessEventsResponseModel extends IModel {
  
  public static final String LIST  = "list";
  public static final String COUNT = "count";
  
  public Long getCount();
  
  public void setCount(Long count);
  
  public List<IProcessEventModel> getList();
  
  public void setList(List<IProcessEventModel> list);
}
