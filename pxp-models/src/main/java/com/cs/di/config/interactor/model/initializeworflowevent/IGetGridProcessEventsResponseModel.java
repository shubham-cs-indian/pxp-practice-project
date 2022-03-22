package com.cs.di.config.interactor.model.initializeworflowevent;

import com.cs.core.config.interactor.model.processevent.IGetConfigDetailsModelForProcess;
import com.cs.core.config.interactor.model.processevent.IGetProcessEventModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetGridProcessEventsResponseModel extends IModel {
  
  public static final String PROCESS_EVENTS_LIST = "processEventsList";
  public static final String COUNT               = "count";
  public static final String CONFIG_DETAILS      = "configDetails";
  
  public List<IGetProcessEventModel> getProcessEventsList();
  
  public void setProcessEventsList(List<IGetProcessEventModel> taskList);
  
  public Long getCount();
  
  public void setCount(Long count);
  
  public IGetConfigDetailsModelForProcess getConfigDetails();
  
  public void setConfigDetails(IGetConfigDetailsModelForProcess configDetails);
}
