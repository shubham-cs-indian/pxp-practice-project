package com.cs.core.config.interactor.model.component;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ITransferDataComponentRequestModel extends IModel {
  
  public static final String EVENT_INSTANCE_ID = "eventInstanceId";
  
  public String getEventInstanceId();
  
  public void setEventInstanceId(String eventInstanceId);
}
