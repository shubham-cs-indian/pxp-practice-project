package com.cs.di.workflow.model.executionstatus;

import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;

import java.io.Serializable;

public interface IOutputExecutionStatusModel extends Serializable{
  
  public static final String MESSAGE_TYPE = "messageType";
  public static final String MESSAGE_CODE = "messageCode";
  public static final String OBJECT_CODES  = "objectCodes";
  public static final String OBJECT_VALUES  = "objectValues";
  public static final String MESSAGE_TEXT = "messageText";
  
  public MessageCode getMessageCode();
  
  public void setMessageCode(MessageCode messageCode);
  
  public String getMessageType();
  
  public void setMessageType(String messageType);
  
  public ObjectCode[] getObjectCodes();
  
  public void setObjectCodes(ObjectCode[] objectCodes);
 
  public String[] getObjectValues();
  
  public void setObjectValues(String[] objectValues);
}
