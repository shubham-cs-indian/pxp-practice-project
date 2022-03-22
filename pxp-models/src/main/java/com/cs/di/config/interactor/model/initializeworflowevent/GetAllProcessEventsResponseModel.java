package com.cs.di.config.interactor.model.initializeworflowevent;

import com.cs.core.config.interactor.model.processevent.IProcessEventModel;
import com.cs.core.config.interactor.model.processevent.ProcessEventModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class GetAllProcessEventsResponseModel implements IGetAllProcessEventsResponseModel {
  
  private static final long          serialVersionUID = 1L;
  protected List<IProcessEventModel> list;
  protected Long                     count;
  
  @Override
  public List<IProcessEventModel> getList()
  {
    return list;
  }
  
  @Override
  @JsonDeserialize(contentAs = ProcessEventModel.class)
  public void setList(List<IProcessEventModel> list)
  {
    this.list = list;
  }
  
  @Override
  public Long getCount()
  {
    return count;
  }
  
  @Override
  public void setCount(Long count)
  {
    this.count = count;
  }
}
