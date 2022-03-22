package com.cs.core.runtime.interactor.model.dataintegration;

public class JMSConfigModel implements IJMSConfigModel {
  
  private static final long serialVersionUID = 1L;
  private String            ip;
  private String            port;
  private String            queueName;
  
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
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((ip == null) ? 0 : ip.hashCode());
    result = prime * result + ((port == null) ? 0 : port.hashCode());
    result = prime * result + ((queueName == null) ? 0 : queueName.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    JMSConfigModel other = (JMSConfigModel) obj;
    if (ip == null) {
      if (other.ip != null)
        return false;
    }
    else if (!ip.equals(other.ip))
      return false;
    if (port == null) {
      if (other.port != null)
        return false;
    }
    else if (!port.equals(other.port))
      return false;
    if (queueName == null) {
      if (other.queueName != null)
        return false;
    }
    else if (!queueName.equals(other.queueName))
      return false;
    return true;
  }
}
