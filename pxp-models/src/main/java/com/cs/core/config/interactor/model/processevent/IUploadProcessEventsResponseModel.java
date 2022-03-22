package com.cs.core.config.interactor.model.processevent;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IUploadProcessEventsResponseModel extends IModel {
  
  public static final String PROCESS_EVENTS_LIST = "processEventsList";
  
  public List<IProcessEventModel> getProcessEventsList();
  
  public void setProcessEventsList(List<IProcessEventModel> taskList);
  
}
