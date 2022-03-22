package com.cs.core.runtime.interactor.model.dataintegration;

public interface IDiQueueInfoModel {
  
  public static String IP             = "ip";
  public static String PORT           = "port";
  public static String QUEUE_NAME     = "queueName";
  public static String USE_CASE       = "useCase";
  public static String PROCESS        = "process";
  public static String ACK_QUEUE_NAME = "acknowledgemenQueueName";
  public static String COMPONENT_ID   = "componentID";
  
  public String getAcknowledgemenQueueName();
  
  public void setAcknowledgemenQueueName(String acknowledgemenQueueName);
  
  public String getUseCase();
  
  public void setUseCase(String useCase);
  
  public String getQueueName();
  
  public void setQueueName(String queueName);
  
  public String getProcess();
  
  public void setProcess(String process);
  
  public String getIp();
  
  public void setIp(String ip);
  
  public String getPort();
  
  public void setPort(String port);
  
  public String getComponentID();
  
  public void setComponentID(String componentID);
}
