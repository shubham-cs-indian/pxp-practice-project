package com.cs.core.runtime.interactor.model.offboarding;

import java.util.ArrayList;
import java.util.List;

public class EventProcessModel implements IEventProcessModel {
  
  private static final long serialVersionUID = 1L;
  protected String          boardingType;
  protected String          endpointId;
  protected String          listenerType;
  protected Object          data;
  protected List<String>    ids;
  
  @Override
  public String getBoardingType()
  {
    return boardingType;
  }
  
  @Override
  public void setBoardingType(String boardingType)
  {
    this.boardingType = boardingType;
  }
  
  @Override
  public List<String> getIds()
  {
    if (this.ids == null) {
      return new ArrayList<>();
    }
    return this.ids;
  }
  
  @Override
  public void setIds(List<String> ids)
  {
    this.ids = ids;
  }
  
  @Override
  public void setAdditionalProperty(String key, Object value)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Object getAdditionalProperty(String key)
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public String getEndpointId()
  {
    return endpointId;
  }
  
  @Override
  public void setEndpointId(String endpointId)
  {
    this.endpointId = endpointId;
  }
  
  @Override
  public Object getData()
  {
    return data;
  }
  
  @Override
  public void setData(Object data)
  {
    this.data = data;
  }
  
  public String getListenerType()
  {
    return listenerType;
  }
  
  public void setListenerType(String listenerType)
  {
    this.listenerType = listenerType;
  }
}
