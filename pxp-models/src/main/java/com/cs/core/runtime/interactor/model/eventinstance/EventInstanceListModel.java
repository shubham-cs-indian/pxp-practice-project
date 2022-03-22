package com.cs.core.runtime.interactor.model.eventinstance;

import com.cs.core.runtime.interactor.entity.eventinstance.EventInstance;
import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class EventInstanceListModel implements IEventInstanceListModel {
  
  private static final long      serialVersionUID = 1L;
  
  protected List<IEventInstance> list             = new ArrayList<>();
  
  @Override
  public List<IEventInstance> getList()
  {
    return list;
  }
  
  @JsonDeserialize(contentAs = EventInstance.class)
  @Override
  public void setList(List<IEventInstance> list)
  {
    this.list = list;
  }
}
