package com.cs.core.runtime.interactor.model.dataintegration;

public class AcknowledgementHeadersModel implements IAcknowledgementHeadersModel {
  
  private static final long serialVersionUID = 1L;
  private String            JMSCorrelationID;
  private String            JMSDeliveryMode;
  private String            JMSExpiration;
  private String            JMSMessageID;
  private String            JMSPriority;
  private String            JMSRedelivered;
  private String            JMSReplyTo;
  private String            JMSDestination;
  private Long              JMSTimestamp;
  private String            JMSType;
  
  @Override
  public String getJMSCorrelationID()
  {
    return JMSCorrelationID;
  }
  
  @Override
  public void setJMSCorrelationID(String jMSCorrelationID)
  {
    JMSCorrelationID = jMSCorrelationID;
  }
  
  @Override
  public String getJMSDeliveryMode()
  {
    return JMSDeliveryMode;
  }
  
  @Override
  public void setJMSDeliveryMode(String jMSDeliveryMode)
  {
    JMSDeliveryMode = jMSDeliveryMode;
  }
  
  @Override
  public String getJMSExpiration()
  {
    return JMSExpiration;
  }
  
  @Override
  public void setJMSExpiration(String jMSExpiration)
  {
    JMSExpiration = jMSExpiration;
  }
  
  @Override
  public String getJMSMessageID()
  {
    return JMSMessageID;
  }
  
  @Override
  public void setJMSMessageID(String jMSMessageID)
  {
    JMSMessageID = jMSMessageID;
  }
  
  @Override
  public String getJMSPriority()
  {
    return JMSPriority;
  }
  
  @Override
  public void setJMSPriority(String jMSPriority)
  {
    JMSPriority = jMSPriority;
  }
  
  @Override
  public String getJMSRedelivered()
  {
    return JMSRedelivered;
  }
  
  @Override
  public void setJMSRedelivered(String jMSRedelivered)
  {
    JMSRedelivered = jMSRedelivered;
  }
  
  @Override
  public String getJMSReplyTo()
  {
    return JMSReplyTo;
  }
  
  @Override
  public void setJMSReplyTo(String jMSReplyTo)
  {
    JMSReplyTo = jMSReplyTo;
  }
  
  @Override
  public String getJMSDestination()
  {
    return JMSDestination;
  }
  
  @Override
  public void setJMSDestination(String jMSDestination)
  {
    JMSDestination = jMSDestination;
  }
  
  @Override
  public Long getJMSTimestamp()
  {
    return JMSTimestamp;
  }
  
  @Override
  public void setJMSTimestamp(Long jMSTimestampaders)
  {
    JMSTimestamp = jMSTimestampaders;
  }
  
  @Override
  public String getJMSType()
  {
    return JMSType;
  }
  
  @Override
  public void setJMSType(String jMSType)
  {
    JMSType = jMSType;
  }
}
