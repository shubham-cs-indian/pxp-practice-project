package com.cs.core.runtime.interactor.model.camunda;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ICamundaJMSListenerModel extends IModel {
  
  public String getIp();
  
  public void setIp(String ip);
  
  public String getPort();
  
  public void setPort(String port);
  
  public String getQueueName();
  
  public void setQueueName(String queueName);
  
  public String getAcknowledgementQueueName();
  
  public void setAcknowledgementQueueName(String acknowledgementQueueName);
  
  public String getListenerType();
  
  public void setListenerType(String listenerType);
  
  public String getEventName();
  
  public void setEventName(String eventName);
}
