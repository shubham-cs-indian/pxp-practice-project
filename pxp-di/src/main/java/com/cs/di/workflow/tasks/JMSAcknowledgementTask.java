package com.cs.di.workflow.tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.stereotype.Component;

import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.component.ActiveMQConfig;
import com.cs.core.runtime.interactor.model.dataintegration.IAcknowledgementBodyModel;
import com.cs.core.runtime.interactor.model.dataintegration.IAcknowledgementPropertiesModel;
import com.cs.core.runtime.interactor.model.dataintegration.IJMSConfigModel;
import com.cs.core.runtime.interactor.model.dataintegration.JMSConfigModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.di.runtime.utils.DiValidationUtil;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.di.workflow.model.executionstatus.IExecutionStatus;
import com.cs.di.workflow.model.executionstatus.IOutputExecutionStatusModel;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.TaskType;
import com.cs.workflow.base.WorkflowType;

@Component("JMSAcknowledgementTask")
public class JMSAcknowledgementTask extends AbstractTask {
  
  public static final String             MESSAGE_PROPERTIES = "MESSAGE_PROPERTIES";
  public static final String             MESSAGE_HEADERS    = "MESSAGE_HEADERS";
  public static final String             SOURCE             = "source";
  

  public static final List<String>       INPUT_LIST           = Arrays.asList(MESSAGE_HEADERS, MESSAGE_PROPERTIES);
  public static final List<String>       OUTPUT_LIST          = Arrays.asList(EXECUTION_STATUS);
  public static final List<WorkflowType> WORKFLOW_TYPES       = Arrays.asList(WorkflowType.JMS_WORKFLOW);

  @Autowired
  ActiveMQConfig             activeMQConfig;
  
  @Override
  public List<String> getInputList()
  {
    return INPUT_LIST;
  }
  
  @Override
  public List<String> getOutputList()
  {
    return OUTPUT_LIST;
  }
  
  @Override
  public List<WorkflowType> getWorkflowTypes()
  {
    return WORKFLOW_TYPES;
  }
  
  @Override
  public List<EventType> getEventTypes()
  {
    return new ArrayList<EventType>();
  }
  
  @Override
  public TaskType getTaskType()
  {
    return TaskType.SERVICE_TASK;
  }
  
  @Override
  @SuppressWarnings("unchecked")
  public void executeTask(WorkflowTaskModel workflowTaskModel)
  {
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = workflowTaskModel.getExecutionStatusTable();
    Map<String, Object> messageHeaders = (Map<String, Object>) DiValidationUtil.validateAndGetRequiredMap(workflowTaskModel, "MESSAGE_HEADERS");
    Map<String, Object> messageProperties = (Map<String, Object>) DiValidationUtil.validateAndGetRequiredMap(workflowTaskModel, "MESSAGE_PROPERTIES");
    if (executionStatusTable.isErrorOccurred()) {
      return;
    }
    try {
      JMSConfigModel replyTo = (JMSConfigModel) messageHeaders.get(JmsHeaders.REPLY_TO);
      if (replyTo != null) {
        send(replyTo, messageHeaders, messageProperties);
        executionStatusTable.addSuccess(new ObjectCode[] { ObjectCode.MESSAGE_TYPE }, MessageCode.GEN047, new String[] {});
      }
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
      executionStatusTable.addError(new ObjectCode[] { ObjectCode.MESSAGE_TYPE }, MessageCode.GEN500, new String[] {});
    }
  }
  
  /**
   * 
   * @param destination
   * @param messageProperties 
   * @param messageHeaders 
   * @param correlationId
   * @return
   * @throws Exception
   */
  public String send(IJMSConfigModel destination, Map<String, Object> messageHeaders, Map<String, Object> messageProperties) throws Exception
  {
    activeMQConfig.getLock().readLock().lock();
    String brokerURL = destination.getIp() + ":" + destination.getPort();
    Map<String, String> messageDataMap = new HashMap<>();
    messageDataMap.put(IAcknowledgementBodyModel.STATUS, "success");
    List<Object> messageList = new ArrayList<>();
    messageList.add(messageDataMap);
    String messageData = ObjectMapperUtil.writeValueAsString(messageDataMap);
    try {
      activeMQConfig.getTemplate(brokerURL).convertAndSend(destination.getQueueName(), messageData, new MessagePostProcessor()
          {
            @Override
            public Message postProcessMessage(Message message) throws JMSException
            {
              message.setJMSCorrelationID((String) messageHeaders.get(JmsHeaders.CORRELATION_ID));
              message.setJMSType((String) messageHeaders.get(JmsHeaders.TYPE));
              message.setJMSTimestamp(Long.parseLong((String) messageHeaders.get(JmsHeaders.TIMESTAMP)));
              message.setStringProperty(IAcknowledgementPropertiesModel.SOURCE, (String) messageProperties.get(SOURCE));

              return message;
            }
          });
      return null;
    }
    
    finally {
      activeMQConfig.getLock().readLock().unlock();
    }
  }
  
  @Override
  public List<String> validate(Map<String, Object> inputFields)
  {
    // TODO Auto-generated method stub
    return null;
  }
}
