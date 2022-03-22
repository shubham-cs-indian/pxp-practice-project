package com.cs.di.jms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.collections.CollectionUtils;
import org.camunda.bpm.engine.ProcessEngineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.constants.CommonConstants;
import com.cs.constants.Constants;
import com.cs.core.config.interactor.entity.processevent.IProcessEvent;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.usecase.processevent.IGetProcessByConfigStrategy;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.exception.camunda.WorkflowEngineException;
import com.cs.core.runtime.interactor.exception.process.WorkflowNotFoundException;
import com.cs.core.runtime.interactor.model.dataintegration.IJMSConfigModel;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.di.runtime.utils.DiUtils;
import com.cs.di.workflow.trigger.IWorkflowParameterModel;
import com.cs.di.workflow.trigger.IWorkflowTriggerModel;
import com.cs.di.workflow.trigger.jms.JMSWorkflowTriggerModel;
import com.cs.workflow.camunda.IBPMNEngineService;


@Service
public class JMSMessageListenerService extends AbstractJMSService implements IJMSMessageListenerService {
    protected Map<String, String> processIdVsProcessDefIdMap = new HashMap<>();

    @Autowired
    protected IBPMNEngineService      bpmnService;
    
  @Autowired
  IGetProcessByConfigStrategy       getProcessByConfigStrategy;
  
      @Override
      public void replaceJMSListener(IJMSConfigModel oldJMSConfig, boolean oldIsActive, IJMSConfigModel newJMSConfig, boolean newIsActive, String processId, String processDefId) {
        if (!newJMSConfig.equals(oldJMSConfig)) {
              removeJMSListener(oldJMSConfig, processId);
              addJMSListener(newJMSConfig, processId, newIsActive);
          } else if(oldIsActive!=newIsActive) {
              if(newIsActive)
                  tryActivating(newJMSConfig, processId);
              else
                  tryDeactivating(newJMSConfig, processId);
          }

        if(newJMSConfig==null || !isValidJMSConfig(newJMSConfig))
              this.processIdVsProcessDefIdMap.remove(processId);
          else
              this.processIdVsProcessDefIdMap.put(processId, processDefId);

          if (!newIsActive) {
              removeJMSListener(oldJMSConfig, processId);
          }
      }

      @Override
      protected MessageListener createJMSListener(IJMSConfigModel destination) {
          return (message)->{
              if (message instanceof TextMessage) {
                  try {
                      RDBMSLogger.instance().info("Message Received in jms receiver");
                      Map<String, Object> workflowParameters = new HashMap<>();
                      Map<String, Object> messageHeaders = extractMessageHeaders(message, destination);
                      Map<String, String> messageProperties = extractMessageProperties(message);
                      workflowParameters.put("MESSAGE_HEADERS", messageHeaders);
                      workflowParameters.put("MESSAGE_PROPERTIES", messageProperties);
                      String text = ((TextMessage) message).getText();
                      int counter=0;Map<String, Object> dataMap = new HashMap<>();
                      String namePrefix = destination.getQueueName()+"_"+System.currentTimeMillis(); 
                      dataMap.put(namePrefix, text);
                      workflowParameters.put("MESSAGE_BODY", dataMap);
                      
                      IWorkflowTriggerModel model = new JMSWorkflowTriggerModel();
                      model.setProcessIds(getProcessDefinitionIds(getActiveProcessIdsForJMSConfig(destination)));
                      IListModel<IWorkflowParameterModel> qualifyingWorkflows = getProcessByConfigStrategy.execute(model);
                      
                      executeQualifyingJMSWorkflows(qualifyingWorkflows, workflowParameters);
                  }
                  catch (Exception ex) {
                      RDBMSLogger.instance().info("Error in File : JMSMessageListenerService");
                      RDBMSLogger.instance().exception(ex);
                      throw new RuntimeException(ex);
                  }
              }
              else {
                  throw new IllegalArgumentException("Message Error");
              }
          };
      }

  /**
   * @param workflowsToExecute
   * @param workflowParameters
   * @throws WorkflowEngineException
   */
  private void executeQualifyingJMSWorkflows(IListModel<IWorkflowParameterModel> workflowsToExecute, Map<String, Object> workflowParameters)
      throws WorkflowEngineException
  {
    try {
      if (workflowsToExecute.getList().isEmpty()) {
        throw new WorkflowNotFoundException("Workflow not found for specified trigger criteria");
      }
      for (IWorkflowParameterModel processMap : workflowsToExecute.getList()) {
        
        Map<String, Object> workflowParameterMap = processMap.getWorkflowParameterMap();
        setTransactionData(workflowParameterMap, workflowParameters);
        workflowParameters.put(IWorkflowParameterModel.WORKFLOW_PARAMETER_MAP, workflowParameterMap);
        bpmnService.startProcessInstanceById((String) workflowParameterMap.get(IProcessEvent.PROCESS_DEFINITION_ID), workflowParameters);
      }
    }
    catch (WorkflowNotFoundException e) {
      RDBMSLogger.instance().debug(e.getMessage());
    }
    catch (ProcessEngineException e) {
      throw new WorkflowEngineException(e);
    }
  }
      
  /**
   * set endpointId and physicalCatalogId that configured on the endpoint in the transaction data.
   * 
   * @param workflowParameterMap
   * @param workflowParameters 
   */
  private void setTransactionData(Map<String, Object> workflowParameterMap, Map<String, Object> workflowParameters)
  {
    ITransactionData   transactionData            = DiUtils.createTransactionData();
    if (workflowParameterMap.get(CommonConstants.PHYSICAL_CATALOG_ID).equals(Constants.DATA_INTEGRATION_CATALOG_IDS)) {
      transactionData.setEndpointId((String) workflowParameterMap.get(CommonConstants.ENDPOINT_ID));
    }
    
    transactionData.setPhysicalCatalogId((String) workflowParameterMap.get(CommonConstants.PHYSICAL_CATALOG_ID));
    List<String> organizationIds = (List<String>) workflowParameterMap.get(IProcessEvent.ORGANIZATIONS_IDS);
    transactionData.setOrganizationId(CollectionUtils.isEmpty(organizationIds) ? null : organizationIds.get(0));
    workflowParameters.put(CommonConstants.TRANSACTION_DATA, transactionData);
  }

      private Set<String> getProcessDefinitionIds(Set<String> activeProcessIdsForJMSConfig) {
          return activeProcessIdsForJMSConfig.stream().map((processId->processIdVsProcessDefIdMap.get(processId))).collect(Collectors.toSet());
      }
}
