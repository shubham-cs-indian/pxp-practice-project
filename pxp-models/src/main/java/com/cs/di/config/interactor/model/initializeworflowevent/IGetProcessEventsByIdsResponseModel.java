package com.cs.di.config.interactor.model.initializeworflowevent;

import com.cs.core.config.interactor.model.processevent.IProcessEventModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IGetProcessEventsByIdsResponseModel extends IModel {
  
  public static final String PROCESS_EVENTS = "processEvents";
  
  public Map<String, IProcessEventModel> getProcessEvents();
  
  public void setProcessEvents(Map<String, IProcessEventModel> processEvents);
}
