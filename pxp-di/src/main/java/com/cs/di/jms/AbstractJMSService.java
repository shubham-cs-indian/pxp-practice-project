package com.cs.di.jms;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.JmsHeaders;

import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.component.ActiveMQConfig;
import com.cs.core.runtime.interactor.model.dataintegration.IJMSConfigModel;
import com.cs.core.runtime.interactor.model.dataintegration.JMSConfigModel;
import com.cs.workflow.camunda.IBPMNEngineService;

public abstract class AbstractJMSService implements IJMSListenerService {

  @Autowired
  protected IBPMNEngineService bpmnService;

  @Autowired
  private ActiveMQConfig activeMQConfig;

  private class JMSListenerContainer {
      private DefaultMessageListenerContainer listenerContainer;
      private Set<String> processIds = new HashSet<>();
      private Set<String> activeProcessIds = new HashSet<>();

      JMSListenerContainer(DefaultMessageListenerContainer listenerContainer) {
          this.listenerContainer = listenerContainer;
      }
  }

  protected Map<IJMSConfigModel, JMSListenerContainer> jmsListenerContainerMap = new HashMap<>();

  protected void addJMSListener(IJMSConfigModel jmsConfig, String processId, boolean isActive) {
      if (!isValidJMSConfig(jmsConfig)) return;

      try {
          JMSListenerContainer jmsListenerContainer = jmsListenerContainerMap.get(jmsConfig);
          if(jmsListenerContainer==null) {
              DefaultMessageListenerContainer listenerContainer = activeMQConfig.getJmsListenerContainer(jmsConfig, createJMSListener(jmsConfig));
              jmsListenerContainer = new JMSListenerContainer(listenerContainer);
              jmsListenerContainer.processIds.add(processId);
              jmsListenerContainerMap.put(jmsConfig, jmsListenerContainer);
              System.out.println("********** Listener Added : " + jmsConfig.getQueueName());
          } else {
              jmsListenerContainer.processIds.add(processId);
          }
          if(isActive)
              tryActivating(processId, jmsListenerContainer);
          else
              tryDeactivating(jmsConfig, processId, jmsListenerContainer);
      } catch (JMSException e) {
          e.printStackTrace();
          RDBMSLogger.instance().exception(e);
      } catch (NamingException e) {
          e.printStackTrace();
          RDBMSLogger.instance().exception(e);
      }
  }
  
  @Override
  public Object getDestination(String ip, String port, String queueName) {
    
    IJMSConfigModel jmsConfigModel = new JMSConfigModel();
    jmsConfigModel.setIp(ip);
    jmsConfigModel.setPort(port);
    jmsConfigModel.setQueueName(queueName);
    return isValidJMSConfig(jmsConfigModel) ? getDestination(jmsConfigModel) : null;
  }

  private Destination getDestination(IJMSConfigModel jmsConfigModel)
  {
    if (jmsListenerContainerMap.containsKey(jmsConfigModel))
      return activeMQConfig.getDestinaion(jmsConfigModel);
    return null;
  }

  protected void tryActivating(IJMSConfigModel jmsConfig, String processId) {
      if (!isValidJMSConfig(jmsConfig)) return;

      JMSListenerContainer jmsListenerContainer = jmsListenerContainerMap.get(jmsConfig);
      tryActivating(processId, jmsListenerContainer);
  }

  private void tryActivating(String processId, JMSListenerContainer jmsListenerContainer) {
      synchronized (jmsListenerContainer) {
          jmsListenerContainer.activeProcessIds.add(processId);
          if (jmsListenerContainer.activeProcessIds.size() == 1)
              jmsListenerContainer.listenerContainer.start();
      }
  }

  protected void removeJMSListener(IJMSConfigModel jmsConfig, String processId) {
      if (!isValidJMSConfig(jmsConfig)) return;

      JMSListenerContainer jmsListenerContainer = jmsListenerContainerMap.get(jmsConfig);
      if(jmsListenerContainer==null) {
          return;
      }
      jmsListenerContainer.processIds.remove(processId);
      tryDeactivating(jmsConfig, processId, jmsListenerContainer);
  }

  protected void tryDeactivating(IJMSConfigModel jmsConfig, String processId) {
      if (!isValidJMSConfig(jmsConfig)) return;

      JMSListenerContainer jmsListenerContainer = jmsListenerContainerMap.get(jmsConfig);
      tryDeactivating(jmsConfig, processId, jmsListenerContainer);
  }

  private void tryDeactivating(IJMSConfigModel jmsConfig, String processId, JMSListenerContainer jmsListenerContainer) {
      synchronized (jmsListenerContainer) {
          if (jmsListenerContainer.activeProcessIds.contains(processId)) {
              jmsListenerContainer.activeProcessIds.remove(processId);
              if (jmsListenerContainer.activeProcessIds.size() == 0) {
                  jmsListenerContainer.listenerContainer.stop();
                  if (jmsListenerContainer.processIds.size() == 0) {
                      jmsListenerContainerMap.remove(jmsConfig);
                  }
              }
          }
      }
  }

  protected boolean isValidJMSConfig(IJMSConfigModel jmsConfig) {
      if (jmsConfig == null || isBlank(jmsConfig.getIp()) || isBlank(jmsConfig.getPort()) || isBlank(jmsConfig.getQueueName()))
          return false;
      return true;
  }

  protected Set<String> getActiveProcessIdsForJMSConfig(IJMSConfigModel jmsConfigModel) {
      return jmsListenerContainerMap.get(jmsConfigModel).activeProcessIds;
  }

  boolean isBlank(String value) {
      return value==null || value.equalsIgnoreCase("");
  }
  
  @Override
  public boolean isInitializationRequired()
  {
    return (jmsListenerContainerMap==null || jmsListenerContainerMap.size()==0);
  }

  protected abstract MessageListener createJMSListener(IJMSConfigModel jmsConfigModel);
  

  protected static Map<String, String> extractMessageProperties(final Message message) {
      final Map<String, String> messageProperties = new HashMap<>();
      try {
          final Enumeration<String> propertyNames = message.getPropertyNames();
          while (propertyNames.hasMoreElements()) {
              String propertyName = propertyNames.nextElement();
              messageProperties.put(propertyName, String.valueOf(message.getObjectProperty(propertyName)));
          }
      } catch (JMSException e) {
      }
      return messageProperties;
  }
  
  protected static Map<String, Object> extractMessageHeaders(final Message message, IJMSConfigModel destination) throws JMSException {
    final Map<String, Object> messageHeaders = new HashMap<>();
    messageHeaders.put(JmsHeaders.DELIVERY_MODE, String.valueOf(message.getJMSDeliveryMode()));
    messageHeaders.put(JmsHeaders.EXPIRATION, String.valueOf(message.getJMSExpiration()));
    messageHeaders.put(JmsHeaders.PRIORITY, String.valueOf(message.getJMSPriority()));
    messageHeaders.put(JmsHeaders.REDELIVERED, String.valueOf(message.getJMSRedelivered()));
    messageHeaders.put(JmsHeaders.TIMESTAMP, String.valueOf(message.getJMSTimestamp()));
    messageHeaders.put(JmsHeaders.CORRELATION_ID, message.getJMSCorrelationID());
    messageHeaders.put(JmsHeaders.MESSAGE_ID, message.getJMSMessageID());
    messageHeaders.put(JmsHeaders.TYPE, message.getJMSType());
    messageHeaders.put(JmsHeaders.DESTINATION, destination);
    String replyToDestinationName = retrieveDestinationName(message.getJMSReplyTo(), JmsHeaders.REPLY_TO);
    if (replyToDestinationName != null) {
        JMSConfigModel replyTo = new JMSConfigModel();
        replyTo.setIp(destination.getIp());
        replyTo.setPort(destination.getPort());
        replyTo.setQueueName(replyToDestinationName);
        messageHeaders.put(JmsHeaders.REPLY_TO, replyTo);
    }
    return messageHeaders;
}

  private static String retrieveDestinationName(Destination destination, String headerName) {
      String destinationName = null;
      if (destination != null) {
          try {
              destinationName = (destination instanceof Queue) ? ((Queue) destination).getQueueName()
                      : ((Topic) destination).getTopicName();
          } catch (JMSException e) {
          }
      }
      return destinationName;
  }
}
