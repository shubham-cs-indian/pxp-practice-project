package com.cs.core.runtime.interactor.model.dataintegration;

public class DiQueueInfoModel implements IDiQueueInfoModel {
  
  private String ip;
  private String port;
  private String queueName;
  private String useCase;
  private String process;
  private String acknowledgemenQueueName;
  private String componentID;
  
  @Override
  public String getComponentID()
  {
    return componentID;
  }
  
  @Override
  public void setComponentID(String componentID)
  {
    this.componentID = componentID;
  }
  
  @Override
  public String getAcknowledgemenQueueName()
  {
    return acknowledgemenQueueName;
  }
  
  @Override
  public void setAcknowledgemenQueueName(String acknowledgemenQueueName)
  {
    this.acknowledgemenQueueName = acknowledgemenQueueName;
  }
  
  @Override
  public String getIp()
  {
    return ip;
  }
  
  @Override
  public void setIp(String ip)
  {
    this.ip = ip;
  }
  
  @Override
  public String getPort()
  {
    return port;
  }
  
  @Override
  public void setPort(String port)
  {
    this.port = port;
  }
  
  @Override
  public String getQueueName()
  {
    return queueName;
  }
  
  @Override
  public void setQueueName(String queueName)
  {
    this.queueName = queueName;
  }
  
  @Override
  public String getUseCase()
  {
    return useCase;
  }
  
  @Override
  public void setUseCase(String useCase)
  {
    this.useCase = useCase;
  }
  
  @Override
  public String getProcess()
  {
    return process;
  }
  
  @Override
  public void setProcess(String process)
  {
    this.process = process;
  }
}
