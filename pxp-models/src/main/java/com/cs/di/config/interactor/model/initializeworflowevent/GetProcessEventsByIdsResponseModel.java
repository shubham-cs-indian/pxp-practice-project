package com.cs.di.config.interactor.model.initializeworflowevent;

import com.cs.core.config.interactor.model.processevent.IProcessEventModel;
import com.cs.core.config.interactor.model.processevent.ProcessEventModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class GetProcessEventsByIdsResponseModel implements IGetProcessEventsByIdsResponseModel {
  
  private static final long                 serialVersionUID = 1L;
  protected Map<String, IProcessEventModel> processEvents;
  
  @Override
  public Map<String, IProcessEventModel> getProcessEvents()
  {
    return processEvents;
  }
  
  @JsonDeserialize(contentAs = ProcessEventModel.class)
  @Override
  public void setProcessEvents(Map<String, IProcessEventModel> processEvents)
  {
    this.processEvents = processEvents;
  }
}
