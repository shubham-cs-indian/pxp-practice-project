package com.cs.core.runtime.interactor.usecase.component.jms;

import com.cs.core.runtime.component.ActiveMQConfig;
import com.cs.core.runtime.interactor.constants.application.DiConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
/**
 * The JmsProducerComponent responsible to send the message to any broker QUEUE 
 * @author priyaranjan.kumar
 *
 */
@Component(value = "jmsProducerComponent")
public class JmsProducerComponent {
  
  public static final String JMSTYPE        = "product";
  
  public static final String TRANSACTION_ID = "transactionId";
  
  public static final String SOURCE         = "source";
  
  public static final String IS_LAST_RETRY  = "isLastTry";
  
  @Autowired
  protected ActiveMQConfig activeMQConfig;
  
  /**
   * Deliver message in a Message Broker Queue 
   * 
   * @param brokerURL this the Broker URL. 
   * @param destination this the Queue Name to send the message
   * @param messageData  this the Actual message data.
   * @param transactionId this the Unique identifier for message 
   * @return
   * @throws Exception
   */
  public String send(String brokerURL, String destination, String messageData, String transactionId,
      Boolean isLastRetry, Object replyTo, String messageType) throws Exception
  {
    activeMQConfig.getLock().readLock().lock();
    try {
      activeMQConfig.getTemplate(brokerURL)
          .convertAndSend(destination, messageData, new MessagePostProcessor()
          {
            @Override
            public Message postProcessMessage(Message message) throws JMSException
            {
              message.setJMSType(messageType);
              if(replyTo != null)
                message.setJMSReplyTo((Destination)replyTo);
              message.setJMSCorrelationID(transactionId);
              message.setStringProperty(SOURCE, DiConstants.PIM);
              message.setBooleanProperty(IS_LAST_RETRY, isLastRetry);
              return message;
            }
          });
      return null;
    }
    
    finally {
      activeMQConfig.getLock().readLock().unlock();
    }
  }
}
