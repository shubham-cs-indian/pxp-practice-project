package com.cs.core.runtime.interactor.model.offboarding;

import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IEventProcessModel extends IIdsListParameterModel {
  
  public static final String BOARDING_TYPE = "boardingType";
  public static final String ENDPOINT_ID   = "endpointId";
  public static final String LISTENER_TYPE = "listenerType";
  public static final String DATA          = "data";
  
  public String getBoardingType();
  
  public void setBoardingType(String boardingType);
  
  public String getEndpointId();
  
  public void setEndpointId(String endpointId);
  
  public Object getData();
  
  public void setData(Object data);
  
  public String getListenerType();
  
  public void setListenerType(String listenerType);
}
