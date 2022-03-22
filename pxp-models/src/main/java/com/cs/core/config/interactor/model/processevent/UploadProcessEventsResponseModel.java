package com.cs.core.config.interactor.model.processevent;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class UploadProcessEventsResponseModel implements IUploadProcessEventsResponseModel {
  
  private static final long       serialVersionUID = 1L;
  
  public List<IProcessEventModel> processEventsList;
  
  @Override
  public List<IProcessEventModel> getProcessEventsList()
  {
    if (processEventsList == null) {
      processEventsList = new ArrayList<>();
    }
    return processEventsList;
  }
  
  @JsonDeserialize(contentAs = GetProcessEventModel.class)
  @Override
  public void setProcessEventsList(List<IProcessEventModel> taskList)
  {
    this.processEventsList = taskList;
  }
  
}
