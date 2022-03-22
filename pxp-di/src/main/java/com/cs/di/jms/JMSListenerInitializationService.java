package com.cs.di.jms;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.processevent.IGetProcessEventModel;
import com.cs.core.config.strategy.usecase.processevent.IGetAllJMSConsumerStrategy;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.constants.application.ProcessConstants;
import com.cs.core.runtime.interactor.model.dataintegration.IJMSConfigModel;
import com.cs.core.runtime.interactor.model.dataintegration.JMSConfigModel;
import com.cs.workflow.base.WorkflowType;
import com.cs.workflow.camunda.CamundaTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class JMSListenerInitializationService implements IJMSService, IJMSListenerInitializationService {


  @Autowired
  protected IGetAllJMSConsumerStrategy               getAllJMSConsumerStrategy;
  
  @Autowired
  protected IJMSMessageListenerService               jmsMessageListenerService;
  
  @Autowired
  protected IJMSAckListenerService                   jmsAckListenerService;
  
  @Autowired
  protected CamundaTask                              camundaTask;
 
  
  public void initializeListenersMap() {
    try {
      if(!this.isInitializationRequired())
        return;
      
      IListModel<IGetProcessEventModel> consumers = getAllJMSConsumerStrategy.execute();
      for (IGetProcessEventModel processEvent : consumers.getList()) {
        String processDefinitionId = processEvent.getProcessDefinitionId();
        if (processDefinitionId == null) {
          continue;
        }
        Set<IJMSConfigModel>  newJMSConfigList = getJmsConfigModel(processEvent.getProcessDefinition());
        if(processEvent.getWorkflowType().equals(WorkflowType.JMS_WORKFLOW.toString())) {
          IJMSConfigModel jmsConfigModel = prepareJMSConfigModel(processEvent.getIp(), processEvent.getPort(), processEvent.getQueue());
          jmsMessageListenerService.replaceJMSListener(null, false, jmsConfigModel, processEvent.getIsExecutable(),
              processEvent.getId(), processDefinitionId);
        } 
        jmsAckListenerService.replaceJMSListeners(null, false, newJMSConfigList,
            processEvent.getIsExecutable(), processDefinitionId);
      }
    }
    catch (Exception e) {
      System.out.println("JMS can not be initialized as no database found.");
    }
  }

  private IJMSConfigModel prepareJMSConfigModel(String ip, String port, String queue)
  {
    IJMSConfigModel jmsConfigModel = new JMSConfigModel();
    jmsConfigModel.setIp(ip);
    jmsConfigModel.setPort(port);
    jmsConfigModel.setQueueName(queue);
    return jmsConfigModel;
  }

  private Set<IJMSConfigModel> getJmsConfigModel(String processDefinition)
  {
    Set<IJMSConfigModel> jmsConfigModels = new HashSet();
    Map<String, Map<String, Object>> allVariables = camundaTask.getAllVariables(processDefinition);
    allVariables.forEach((taskId, variables) -> {
      IJMSConfigModel jmsConfigModel = new JMSConfigModel();
    if (ProcessConstants.DELIVERY_TASK.equals(variables.get(ProcessConstants.TASK_ID))) {
        jmsConfigModel.setIp((String) variables.get(ProcessConstants.CLASS_PATH_IP));
        jmsConfigModel.setPort((String) variables.get(ProcessConstants.PORT));
        jmsConfigModel.setQueueName((String) variables.get(ProcessConstants.ACKNOWLEDGEMENT_QUEUE_NAME));
        jmsConfigModels.add(jmsConfigModel);
      }
    });
    return jmsConfigModels;
  }

  @Override
  public boolean isInitializationRequired()
  {
    return (jmsMessageListenerService.isInitializationRequired() 
        && jmsAckListenerService.isInitializationRequired());
  }

  @Override
  public IJMSConfigModel execute(IJMSConfigModel dataModel) throws Exception
  {
    initializeListenersMap();
    return null;
  }

  @Override
  public ServiceType getServiceType()
  {
    return ServiceType.GET;
  }
  
}
