package com.cs.core.runtime.interactor.model.dataintegration;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IAcknowledgementHeadersModel extends IModel {
  
  public static final String JMS_CORRELATION_ID = "JMSCorrelationID";
  public static final String JMS_DELIVERY_MODE  = "JMSDeliveryMode";
  public static final String JMS_EXPIRATION     = "JMSExpiration";
  public static final String JMS_MESSAGE_ID     = "JMSMessageID";
  public static final String JMS_PRIORITY       = "JMSPriority";
  public static final String JMS_REDELIVERED    = "JMSRedelivered";
  public static final String JMS_REPLY_TO       = "JMSReplyTo";
  public static final String JMS_DESTINATION    = "JMSDestination";
  public static final String JMS_TIMESTAMP      = "JMSTimestamp";
  public static final String JMS_TYPE           = "JMSType";
  
  public String getJMSCorrelationID();
  
  public void setJMSCorrelationID(String jMSCorrelationID);
  
  public String getJMSDeliveryMode();
  
  public void setJMSDeliveryMode(String jMSDeliveryMode);
  
  public String getJMSExpiration();
  
  public void setJMSExpiration(String jMSExpiration);
  
  public String getJMSMessageID();
  
  public void setJMSMessageID(String jMSMessageID);
  
  public String getJMSPriority();
  
  public void setJMSPriority(String jMSPriority);
  
  public String getJMSRedelivered();
  
  public void setJMSRedelivered(String jMSRedelivered);
  
  public String getJMSReplyTo();
  
  public void setJMSReplyTo(String jMSReplyTo);
  
  public String getJMSDestination();
  
  public void setJMSDestination(String jMSDestination);
  
  public Long getJMSTimestamp();
  
  public void setJMSTimestamp(Long jMSTimestampaders);
  
  public String getJMSType();
  
  public void setJMSType(String jMSType);
}
