
package com.cs.di.config.businessapi.processevent;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.xml.instance.DomDocument;
import org.camunda.bpm.model.xml.instance.DomElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.model.processevent.CreateOrSaveProcessEventResponseModel;
import com.cs.core.config.interactor.model.processevent.GetProcessEventModel;
import com.cs.core.config.interactor.model.processevent.ICreateOrSaveProcessEventResponseModel;
import com.cs.core.config.interactor.model.processevent.IGetProcessEventModel;
import com.cs.core.config.interactor.model.processevent.IValidationInformationModel;
import com.cs.core.config.interactor.model.processevent.ValidationInformationModel;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.constants.application.ProcessConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.dataintegration.IJMSConfigModel;
import com.cs.core.runtime.interactor.model.dataintegration.JMSConfigModel;
import com.cs.core.runtime.strategy.cammunda.broadcast.IUploadProcessToServerStrategy;
import com.cs.di.config.interactor.model.initializeworflowevent.ISaveProcessEventModel;
import com.cs.di.config.strategy.processevent.ISaveProcessEventStrategy;
import com.cs.di.config.strategy.usecase.processevent.IGetProcessEventStrategy;
import com.cs.di.jms.IJMSAckListenerService;
import com.cs.di.jms.IJMSMessageListenerService;
import com.cs.workflow.base.IAbstractExecutionReader;
import com.cs.workflow.base.IAbstractTask;
import com.cs.workflow.base.TaskType;
import com.cs.workflow.base.TasksFactory;
import com.cs.workflow.base.WorkflowConstants;
import com.cs.workflow.base.WorkflowType;

@Service
public class SaveProcessEventService extends AbstractSaveConfigService<ISaveProcessEventModel, ICreateOrSaveProcessEventResponseModel>
    implements ISaveProcessEventService {
  
  @Autowired
  protected ISaveProcessEventStrategy                saveProcessEventStrategy;

  @Autowired
  protected IGetProcessEventStrategy       getProcessEventStrategy;

  @Autowired
  protected IUploadProcessToServerStrategy uploadProcessToServerStrategy;
  
/*  @Autowired
  protected JMSConsumerConfig                        jMSConsumerConfig;
  
  @Autowired
  protected IGetJMSInfoByProcessDefinitionIdStrategy getJMSInfoByProcessDefinitionIdStrategy;
  */
  @Autowired
  protected IAbstractExecutionReader                 camundaTask;
  
  @Autowired
  protected TasksFactory                             tasksFactory;

  public static final String                         MODEL_NS                = "http://www.omg.org/spec/BPMN/20100524/MODEL";
  public static final String                         START_EVENT             = "startEvent";
  public static final String                         TIMER_EVENT_DEFINITION  = "timerEventDefinition";
  public static final String                         BPMN_TFORMAL_EXPRESSION = "bpmn:tFormalExpression";
  public static final String                         XSI                     = "http://www.w3.org/2001/XMLSchema-instance";
  public static final String                         PROCESS                 = "process";

  @Autowired
  protected IJMSMessageListenerService jmsMessageListenerService;

  @Autowired
  protected IJMSAckListenerService jmsAckListenerService;


  @Override
  public ICreateOrSaveProcessEventResponseModel executeInternal(ISaveProcessEventModel model) throws Exception
  {
    ICreateOrSaveProcessEventResponseModel processEventModel = new CreateOrSaveProcessEventResponseModel();
    IValidationInformationModel validationInfo = null;
    Boolean saveProcess = true;
    // If timer definition is modified then modify the process definition
    if (!StringUtils.isBlank(model.getTimerStartExpression()) && !StringUtils.isBlank(model.getTimerDefinitionType())) {
      updateOrCreateTimerDefination(model);
    }
    if (model.getIsExecutable()) {
      Map<String, Map<String, Object>> taskVariablesMap = camundaTask.readTaskVariablesMap(model.getProcessDefinition(),
          model.getLabel());
      validationInfo = getValidationInfo(model, taskVariablesMap);
      if (validationInfo.getIsWorkflowValid()) {
        if (model.getIsXMLModified()) {
          updateBPMN(model, taskVariablesMap);
          uploadProcessToServerStrategy.execute(model);
        }
      }
      else {
        saveProcess = false;
      }
    }
    if (saveProcess) {
      IIdParameterModel idModel = new IdParameterModel();
      idModel.setId(model.getId());
      IGetProcessEventModel oldProcessEvent = getOldProcessEvent(idModel);;
      processEventModel = saveProcessEventStrategy.execute(model);
      try {
        updateListeners(processEventModel, oldProcessEvent);
      }
      catch (Exception e) {
        RDBMSLogger.instance().exception(e);
      }
    }
    
    processEventModel.setValidationInfo(validationInfo);
    return processEventModel;
  }
  
  private void updateBPMN(ISaveProcessEventModel model, Map<String, Map<String, Object>> taskVariablesMap)
  {
    
    String processDefinition = model.getProcessDefinition();
    processDefinition = camundaTask.updateBPMN(processDefinition, getRuntimeVariables(taskVariablesMap));
    model.setProcessDefinition(processDefinition);
  }
  
  private String getRuntimeVariables(Map<String, Map<String, Object>> taskVariablesMap)
  {
    String runtimeVariables = "";
    String comma = "";
    for (Entry<String, Map<String, Object>> entry : taskVariablesMap.entrySet()) {
      Map<String, Object> taskVariables = entry.getValue();
      for (Entry<String, Object> variableEntry : taskVariables.entrySet()) {
        Object valueObj = variableEntry.getValue();
        if (valueObj == null || !(valueObj instanceof String)) {
          continue;
        }
        String value = (String) valueObj;
        if (!value.isEmpty() && isRuntimeValue(value)) {
          runtimeVariables = runtimeVariables + comma + value.substring(value.indexOf("{", 1) + 1, value.lastIndexOf("}"));
          comma = ",";
        }
      }
    }
    return runtimeVariables;
  }
  
  /**
   * @param model
   * @param taskVariablesMap
   * @return
   * @throws Exception
   */
  private IValidationInformationModel getValidationInfo(ISaveProcessEventModel model, Map<String, Map<String, Object>> taskVariablesMap)
      throws Exception
  {
    Map<String, List<String>> invalidInputList = new HashMap<>();
    IValidationInformationModel validationInformationModel = new ValidationInformationModel(true, invalidInputList);
    if (taskVariablesMap.isEmpty()) {
      validationInformationModel.setIsWorkflowValid(false);
      return validationInformationModel;
    }
    else if (taskVariablesMap.get("allowSave") != null || model.getWorkflowType().equalsIgnoreCase(WorkflowType.HIDDEN_WORKFLOW.name())) {
      return validationInformationModel;
    }
    for (Map.Entry<String, Map<String, Object>> taskVariablesEntry : taskVariablesMap.entrySet()) {
      String uiTaskId = taskVariablesEntry.getKey();
      Map<String, Object> taskVariables = taskVariablesEntry.getValue();
      IAbstractTask task = tasksFactory.getTask((String) taskVariables.get(WorkflowConstants.TASK_ID));
      if (task == null || task.getTaskType() == TaskType.BGP_TASK) {
        continue;
      }
      List<String> invalidInputs = task.validate(taskVariables);
      if (invalidInputs != null && !invalidInputs.isEmpty()) {
        validationInformationModel.setIsWorkflowValid(false);
        invalidInputList.put(uiTaskId, invalidInputs);
      }
    }
    validationInformationModel.setValidationDetails(invalidInputList);
    return validationInformationModel;
  }

  public void updateListeners(IGetProcessEventModel processEventModel, IGetProcessEventModel oldProcessEventModel) throws Exception
  {
    Set<IJMSConfigModel> oldJMSConfigList = getJmsConfigModel(oldProcessEventModel.getProcessDefinition());
    Set<IJMSConfigModel> newJMSConfigList = getJmsConfigModel(processEventModel.getProcessDefinition());
    jmsAckListenerService.replaceJMSListeners(oldJMSConfigList, oldProcessEventModel.getIsExecutable(), newJMSConfigList,
        processEventModel.getIsExecutable(), processEventModel.getId());
    //
    if (WorkflowType.JMS_WORKFLOW.name().equals(processEventModel.getWorkflowType())) {
      IJMSConfigModel jmsConfigModel = getJmsConfigModel(processEventModel);
      IJMSConfigModel oldJmsConfigModel = getJmsConfigModel(oldProcessEventModel);
      jmsMessageListenerService.replaceJMSListener(oldJmsConfigModel, oldProcessEventModel.getIsExecutable(), jmsConfigModel,
          processEventModel.getIsExecutable(), processEventModel.getId(), processEventModel.getProcessDefinitionId());
    }
  }
  private Set<IJMSConfigModel> getJmsConfigModel(String processDefinition)
  {
    Set<IJMSConfigModel> jmsConfigModels = new HashSet();
    if (processDefinition == null || processDefinition.isEmpty()) {
      return jmsConfigModels;
    }
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

  private IJMSConfigModel getJmsConfigModel(IGetProcessEventModel processEventModel)
  {
    IJMSConfigModel jmsConfigModel = new JMSConfigModel();
    if(WorkflowType.JMS_WORKFLOW.name().equals(processEventModel.getWorkflowType())) {
      jmsConfigModel.setIp(processEventModel.getIp());
      jmsConfigModel.setPort(processEventModel.getPort());
      jmsConfigModel.setQueueName(processEventModel.getQueue());
    }
    return jmsConfigModel;
  }

  /**
   * Modify process definition to set the the timer definition and expression
   *
   * @param model
   * @throws Exception
   */
  private void updateOrCreateTimerDefination(ISaveProcessEventModel model) throws Exception
  {
    BpmnModelInstance bpmnModel = Bpmn.readModelFromStream(new ByteArrayInputStream(model.getProcessDefinition().getBytes()));
    DomDocument document = bpmnModel.getDocument();
    DomElement root = document.getRootElement();
    DomElement processElement = parseProcessElement(root, PROCESS);

    DomElement parseProcessElement = parseProcessElement(processElement, START_EVENT);
    DomElement timerDefinitionElement = parseProcessElement(parseProcessElement, TIMER_EVENT_DEFINITION);
    DomElement childElement = document.createElement(MODEL_NS, "bpmn:" + model.getTimerDefinitionType());
    childElement.setAttribute(XSI, "type", BPMN_TFORMAL_EXPRESSION);
    childElement.setTextContent(model.getTimerStartExpression());
    if (timerDefinitionElement != null) {
      if (!timerDefinitionElement.getChildElements().isEmpty()) {
        timerDefinitionElement.replaceChild(childElement, timerDefinitionElement.getChildElements().get(0));
      }
      else {
        timerDefinitionElement.appendChild(childElement);
      }
    }
    model.setProcessDefinition(Bpmn.convertToString(bpmnModel));
  }

  /**
   * @param root - root Dom element of xml
   * @param elementName - Element label
   * @return DemElement with name as "process"
   * @throws Exception
   */
  protected DomElement parseProcessElement(DomElement root, String elementName) throws Exception
  {
    List<DomElement> elements = root.getChildElementsByNameNs(MODEL_NS, elementName);
    if (elements.isEmpty()) {
      throw new Exception("No BPMN Process found in the Workflow : " + elementName);
    }
    if (elements.size() > 1) {
      throw new Exception("Multiple BPMN Processes/Pools found in the Workflow : " + elementName);
    }
    return elements.get(0);
  }

  /**
   * Check runtime value
   * 
   * @param value
   * @return
   */
  //TODO : When AbstarctTask class is moved then removed this method and add method from AbstarctTask
  private boolean isRuntimeValue(String value)
  {
    return value != null && value.startsWith("$");
  }

  private IGetProcessEventModel getOldProcessEvent(IIdParameterModel idModel) throws Exception {
    try {
      return getProcessEventStrategy.execute(idModel);
    } catch (Exception e) {
      GetProcessEventModel model = new GetProcessEventModel();
      model.setIsExecutable(false);
      return model;
    }
  }
}
