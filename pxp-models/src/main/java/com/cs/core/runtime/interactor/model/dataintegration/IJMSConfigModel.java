package com.cs.core.runtime.interactor.model.dataintegration;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IJMSConfigModel extends IModel {
  public static String IP         = "ip";
  public static String PORT       = "port";
  public static String QUEUE_NAME = "queue";
  
  public String getQueueName();
  
  public void setQueueName(String queueName);
  
  public String getPort();
  
  public void setPort(String port);
  
  public String getIp();
  
  public void setIp(String ip);
}
