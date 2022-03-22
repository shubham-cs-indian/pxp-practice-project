package com.cs.core.runtime.component;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.PostConstruct;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.stereotype.Component;

import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.model.dataintegration.IJMSConfigModel;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@Configuration
@Component
public class ActiveMQConfig {
  
  private Cache<String, JmsTemplate> templates;
  
  private ReadWriteLock              lock = new ReentrantReadWriteLock();
  
  /*@Autowired
  protected KafkaProducer            kafkaProducer;*/
  
  @Autowired
  protected String                   jndiContextFactory;
  
  @Autowired
  protected String                   connectionFactoryName;
  
  @Autowired
  protected String                   jmsUsername;
  
  @Autowired
  protected String                   jmsPassword;

  @Autowired
  protected ApplicationContext       appContext;
  
  @Autowired
  protected String                   jmsConcurrency;
  
  @PostConstruct
  public void init()
  {
    templates = CacheBuilder.newBuilder()
        .expireAfterAccess(5, TimeUnit.MINUTES)
        .build();
  }
  
  public JmsTemplate getTemplate(String brokerURL) throws JMSException, NamingException
  {
    JmsTemplate jmsTemplate = templates.getIfPresent(brokerURL);
    if (jmsTemplate == null) {
      templates.put(brokerURL, jmsTemplate(brokerURL));
    }
    return templates.getIfPresent(brokerURL);
  }

  private Map<String, InitialContext> connectionFactoryMap=new HashMap<>();
  private InitialContext getConnectionFactory(String brokerURL)
      throws NamingException, JMSException
  {
    if(connectionFactoryMap.containsKey(brokerURL))
      return connectionFactoryMap.get(brokerURL);
    Properties initialContextProperties = new Properties();
    initialContextProperties.put("java.naming.factory.initial", jndiContextFactory);
    initialContextProperties.put("java.naming.provider.url", brokerURL);
    if (!jmsUsername.isEmpty()) {
      initialContextProperties.put("java.naming.security.principal", jmsUsername);
      initialContextProperties.put("java.naming.security.credential", jmsPassword);
    }
    InitialContext initialContext = new InitialContext(initialContextProperties);
   
    connectionFactoryMap.put(brokerURL, initialContext);
    return initialContext;
  }
  
  private JmsTemplate jmsTemplate(String brokerURL) throws JMSException, NamingException
  {
    ConnectionFactory connectionFactory = (ConnectionFactory) getConnectionFactory(brokerURL)
        .lookup(connectionFactoryName);
    return new JmsTemplate(connectionFactory);
  }
  
  public DefaultMessageListenerContainer getJmsListenerContainer(IJMSConfigModel jmsConfigModel, MessageListener messageListener) throws JMSException, NamingException
  {
    System.out.println("Receiver Componets");
    DefaultMessageListenerContainer dmlc = new DefaultMessageListenerContainer();
    dmlc.setDestinationName(jmsConfigModel.getQueueName());
    InitialContext initialContext = getConnectionFactory(jmsConfigModel.getIp() + ":" + jmsConfigModel.getPort());
    dmlc.setConnectionFactory((ConnectionFactory) initialContext.lookup(connectionFactoryName));
    //dmlc.setDestination((Destination) initialContext.lookup(jmsConfigModel.getQueueName()));
    // To perform actual message processing
    dmlc.setMessageListener(messageListener);
    // dmlc.setMessageListener(new
    // JmsReceiverComponent(destination,endpoint,kafkaProducer));
    dmlc.setConcurrency(jmsConcurrency);
    dmlc.afterPropertiesSet();
    System.out.println("Returning dmlc");
    return dmlc;
  }

  public Destination getDestinaion(IJMSConfigModel jmsConfigModel) {
    try {
      ConnectionFactory connectionFactory = (ConnectionFactory) getConnectionFactory(jmsConfigModel.getIp() + ":" + jmsConfigModel.getPort())
          .lookup(connectionFactoryName);                  
      Connection connection = connectionFactory.createConnection();
      connection.start();
      Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      
      Destination destination = session.createQueue(jmsConfigModel.getQueueName());
      return destination;
    }
    catch (JMSException | NamingException e) {
      e.printStackTrace();
      RDBMSLogger.instance().exception(e);
    }
    return null;

  }
  
  public ReadWriteLock getLock()
  {
    return lock;
  }
  
  public void setLock(ReadWriteLock lock)
  {
    this.lock = lock;
  }
}
