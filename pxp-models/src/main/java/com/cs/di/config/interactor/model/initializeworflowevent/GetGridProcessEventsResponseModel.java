package com.cs.di.config.interactor.model.initializeworflowevent;

import com.cs.core.config.interactor.model.processevent.GetConfigDetailsModelForProcess;
import com.cs.core.config.interactor.model.processevent.GetProcessEventModel;
import com.cs.core.config.interactor.model.processevent.IGetConfigDetailsModelForProcess;
import com.cs.core.config.interactor.model.processevent.IGetProcessEventModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class GetGridProcessEventsResponseModel implements IGetGridProcessEventsResponseModel {
  
  private static final long                  serialVersionUID = 1L;
  protected List<IGetProcessEventModel>      processEventsList;
  protected Long                             count;
  protected IGetConfigDetailsModelForProcess configDetails;
  
  @Override
  public List<IGetProcessEventModel> getProcessEventsList()
  {
    if (processEventsList == null) {
      processEventsList = new ArrayList<>();
    }
    return processEventsList;
  }
  
  @JsonDeserialize(contentAs = GetProcessEventModel.class)
  @Override
  public void setProcessEventsList(List<IGetProcessEventModel> taskList)
  {
    this.processEventsList = taskList;
  }
  
  @Override
  public Long getCount()
  {
    if (count == null) {
      count = new Long(0);
    }
    return count;
  }
  
  @Override
  public void setCount(Long count)
  {
    this.count = count;
  }
  
  @Override
  public IGetConfigDetailsModelForProcess getConfigDetails()
  {
    return configDetails;
  }
  
  @JsonDeserialize(as = GetConfigDetailsModelForProcess.class)
  @Override
  public void setConfigDetails(IGetConfigDetailsModelForProcess configDetails)
  {
    this.configDetails = configDetails;
  }
}
