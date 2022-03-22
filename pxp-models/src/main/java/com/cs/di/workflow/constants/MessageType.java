package com.cs.di.workflow.constants;

public enum MessageType 
{
  SUMMARY("S"),
  ERROR("E"),
  WARNING("W"),
  INFORMATION("I"),
  SUCCESS("S");
  final String messageType;
  
  MessageType(String messageCode)
  {
    this.messageType = messageCode;
  }
  
  public String getName()
  {
    return messageType;
  }
}
