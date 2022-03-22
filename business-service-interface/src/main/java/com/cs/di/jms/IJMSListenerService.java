package com.cs.di.jms;

public interface IJMSListenerService  extends IJMSService{
  Object getDestination(String ip, String port, String queueName);
}
