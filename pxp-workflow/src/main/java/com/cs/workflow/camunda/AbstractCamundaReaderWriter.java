
package com.cs.workflow.camunda;

import com.cs.workflow.base.IAbstractExecutionReader;
import com.cs.workflow.base.TasksFactory;
import org.camunda.bpm.engine.delegate.BpmnModelExecutionContext;
import org.camunda.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaExecutionListener;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaInputOutput;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaInputParameter;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperties;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperty;
import org.camunda.bpm.model.xml.instance.DomElement;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.util.*;

@Component
public abstract class AbstractCamundaReaderWriter implements IAbstractExecutionReader {
  
  private static final String BPMN               = "http://www.omg.org/spec/BPMN/20100524/MODEL";
  private static final String CAMUNDA            = "http://camunda.org/schema/1.0/bpmn";
  private static final String PROCESS            = "process";
  private static final String SERVICE_TASK       = "serviceTask";
  private static final String EXTENSION_ELEMENTS = "extensionElements";
  private static final String INPUT_OUTPUT       = "inputOutput";
  private static final String INPUT_PARAMETER    = "inputParameter";
  private static final String NAME               = "name";
  private static final String ID                 = "id";
  private static final String CALL_ACTIVITY      = "callActivity";
  private static final String START_EVENT        = "startEvent";
  private static final String END_EVENT          = "endEvent";
  private static final String EXECUTION_LISTENER = "executionListener";

  @Autowired
  public TasksFactory tasksFactory;

  @Override
  public Object getVariable(Object execution, String key)
  {
    return ((VariableScope) execution).getVariable(key);
  }
  
  @Override
  public void setVariable(Object execution, String key, Object value)
  {
    if (((VariableScope) execution).hasVariableLocal(key)) {
      ((VariableScope) execution).removeVariableLocal(key);
    }
    ((VariableScope) execution).setVariable(key, value);
  }
  
  /**
   * 
   * Checks if a String is whitespace, empty ("") or null.
   *
   * 
   * StringUtils.isBlank(null) = true StringUtils.isBlank("") = true
   * StringUtils.isBlank(" ") = true StringUtils.isBlank("bob") = false
   * StringUtils.isBlank(" bob ") = false
   *
   * @param str the String to check, may be null
   * @return true if the String is null, empty or whitespace
   *
   */
  public static boolean isBlank(String str)
  {
    int strLen;
    if (str == null || (strLen = str.length()) == 0) {
      return true;
    }
    for (int i = 0; i < strLen; i++) {
      if ((Character.isWhitespace(str.charAt(i)) == false)) {
        return false;
      }
    }
    return true;
  }
  
  @Override
  public Map<String, Map<String, Object>> readTaskVariablesMap(String processDefinition, String label)
  {
    Map<String, Map<String, Object>> taskVariablesMap = new HashMap<String, Map<String, Object>>();
    BpmnModelInstance bpmnModel = Bpmn.readModelFromStream(new ByteArrayInputStream((processDefinition).getBytes()));
    DomElement root = bpmnModel.getDocument().getRootElement();
    List<DomElement> elements = root.getChildElementsByNameNs(BPMN, PROCESS);
    if (elements.isEmpty() || elements.size() > 1) {
      return taskVariablesMap;
    }
    DomElement processElement = elements.get(0);
    List<DomElement> callActivities = processElement.getChildElementsByNameNs(BPMN, CALL_ACTIVITY);
    List<DomElement> serviceTasks = processElement.getChildElementsByNameNs(BPMN, SERVICE_TASK);
    List<DomElement> tasksToBeValidated = new ArrayList<DomElement>();
    tasksToBeValidated.addAll(serviceTasks);
    tasksToBeValidated.addAll(callActivities);
    for (DomElement taskToBeValidated : tasksToBeValidated) {
      Map<String, Object> taskVariables = new HashMap<String, Object>();
      taskVariablesMap.put(taskToBeValidated.getAttribute(ID), taskVariables);
      List<DomElement> extensionElements = taskToBeValidated.getChildElementsByNameNs(BPMN, EXTENSION_ELEMENTS);
      if (extensionElements.isEmpty()) {
        continue;
      }
      List<DomElement> inputOutput = extensionElements.get(0).getChildElementsByNameNs(CAMUNDA, INPUT_OUTPUT);
      if (inputOutput.isEmpty()) {
        continue;
      }
      for (DomElement inputParameter : inputOutput.get(0).getChildElementsByNameNs(CAMUNDA, INPUT_PARAMETER)) {
        List<DomElement> childValues = inputParameter.getChildElements();
        if (!childValues.isEmpty()) {
          List<String> finalValues = new ArrayList<String>();
          taskVariables.put(inputParameter.getAttribute(NAME), finalValues);
          for (DomElement childValue : childValues) {
            switch (childValue.getLocalName()) {
              case "list":
              case "map":
                List<DomElement> entries = childValue.getChildElements();
                for (DomElement entry : entries) {
                  finalValues.add(entry.getTextContent()
                      .trim());
                }
                break;
              
              default:
                String[] values = childValue.getTextContent()
                    .split("\n");
                for (String value : values) {
                  if (!isBlank(value)) {
                    finalValues.add(StringUtils.trimAllWhitespace(value));
                  }
                }
            }
          }
        }
        else {
          taskVariables.put(inputParameter.getAttribute(NAME), inputParameter.getTextContent().trim());
        }
      }
    }
    // For tasks that don't have any input params for validation
    if (taskVariablesMap.isEmpty()) {
      taskVariablesMap.put("allowSave", new HashMap<String, Object>());
    }
    return taskVariablesMap;
  }
  
  @Override
  public String updateBPMN(String processDefinition, String runtimeVariables)
  {
    BpmnModelInstance modelInstance = Bpmn.readModelFromStream(new ByteArrayInputStream((processDefinition).getBytes()));
    String bpmnPrefix = modelInstance.getDocument().getRootElement().lookupPrefix(BPMN);
    updateStartEvent(modelInstance, modelInstance.getModelElementsByType(StartEvent.class).iterator(), runtimeVariables);
    addDelegateExpression(modelInstance, modelInstance.getModelElementsByType(EndEvent.class).iterator(), "end");
    processDefinition = Bpmn.convertToString(modelInstance);
    if (bpmnPrefix != null) {
      processDefinition = processDefinition.replaceAll("<extensionElements xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\">",
          "<" + bpmnPrefix + ":extensionElements>");
      processDefinition = processDefinition.replaceAll("</extensionElements>", "</" + bpmnPrefix + ":extensionElements>");
    }
    else {
      processDefinition = processDefinition.replaceAll("<extensionElements xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\">",
          "<extensionElements>");
    }
    return processDefinition;
  }
  
  private void updateStartEvent(BpmnModelInstance modelInstance, Iterator<? extends BaseElement> baseElementItr, String runtimeVariables)
  {
    if (!baseElementItr.hasNext()) {
      return;
    }
    BaseElement baseElement = baseElementItr.next();
    addDelegateExpression(modelInstance, "start", baseElement);
    
    ExtensionElements extensionElements = baseElement.getExtensionElements();
    Collection<CamundaProperties> camPropertiesCollection = extensionElements.getChildElementsByType(CamundaProperties.class);

    for (CamundaProperties properties:camPropertiesCollection) {
      for(CamundaProperty property: properties.getCamundaProperties()) {
        if(property.getCamundaName().equals(RUNTIME_VARIABLES)) {
          property.setCamundaValue(runtimeVariables);
          return;
        }
      }
    }

    CamundaProperties camundaProperties = null;
    if(camPropertiesCollection.size()>0)
      camundaProperties = camPropertiesCollection.iterator().next();
    else
      camundaProperties = extensionElements.addExtensionElement(CamundaProperties.class);

    addProperty(camundaProperties, RUNTIME_VARIABLES, runtimeVariables);
  }

  private void addProperty(CamundaProperties camundaProperties, String key, String value) {
    CamundaProperty property = camundaProperties.getModelInstance().newInstance(CamundaProperty.class);
    property.setCamundaName(key);
    property.setCamundaValue(value);
    camundaProperties.addChildElement(property);
  }

  private void addDelegateExpression(BpmnModelInstance modelInstance, Iterator<? extends BaseElement> baseElementItr, String camundaEvent)
  {
    if (!baseElementItr.hasNext()) {
      return;
    }
    BaseElement baseElement = baseElementItr.next();
    addDelegateExpression(modelInstance, camundaEvent, baseElement);
  }
  
  private void addDelegateExpression(BpmnModelInstance modelInstance, String camundaEvent, BaseElement baseElement)
  {
    ExtensionElements extensionElements = baseElement.getExtensionElements();
    if (extensionElements == null) {
      extensionElements = modelInstance.newInstance(ExtensionElements.class);
      baseElement.setExtensionElements(extensionElements);
    }
    Iterator itr = extensionElements.getElements().iterator();
    boolean execListenerAdded = false;
    if (itr.hasNext()) {
      ModelElementInstance elementInstance = (ModelElementInstance) itr.next();
      if (elementInstance instanceof CamundaExecutionListener) {
        execListenerAdded = true;
      }
    }
    if (!execListenerAdded) {
      CamundaExecutionListener listener = extensionElements.addExtensionElement(CamundaExecutionListener.class);
      listener.setCamundaDelegateExpression("${camundaTask}");
      listener.setCamundaEvent(camundaEvent);
    }
  }
  
  /**
   * Check runtime value
   * 
   * @param value
   * @return
   */
  public static boolean isRuntimeValue(String value)
  {
    return value != null && value.startsWith("$");
  }
  
  @Override
  public Map<String, String> getProperties(Object execution)
  {
    Map<String, String> properties = new HashMap<>();
    FlowElement flowElement = ((BpmnModelExecutionContext) execution).getBpmnModelElementInstance();
    Collection<CamundaProperties> camPropertiesCollection = flowElement.getExtensionElements()
        .getChildElementsByType(CamundaProperties.class);
    for (CamundaProperties camProperties : camPropertiesCollection) {
      for (CamundaProperty camProperty : camProperties.getCamundaProperties()) {
        properties.put(camProperty.getCamundaName(), camProperty.getCamundaValue());
      }
    }
    return properties;
  }


  
  public Map<String, Map<String, Object>> getAllVariables(String processDefinition)
  {
    Map<String, Map<String, Object>> taskVariablesMap = new HashMap<String, Map<String, Object>>();
    BpmnModelInstance modelInstance = Bpmn.readModelFromStream(new ByteArrayInputStream((processDefinition).getBytes()));
      Collection<ServiceTask> serviceTasks = modelInstance
          .getModelElementsByType(ServiceTask.class);
      for (ServiceTask serviceTask : serviceTasks) {
        Map<String, Object> configMap = getCamundaInputOutputParametersMap(serviceTask);
        String componentId = (String) configMap.get("id");
        taskVariablesMap.put(componentId, configMap);
      }
    return taskVariablesMap;
  }
  
  private Map<String, Object> getCamundaInputOutputParametersMap(ServiceTask serviceTask)
  {
    CamundaInputOutput inputOutput = serviceTask.getExtensionElements()
        .getElementsQuery()
        .filterByType(CamundaInputOutput.class)
        .singleResult();
    
    Map<String, Object> configMap = new HashMap<>();
    configMap.put("id", serviceTask.getId());
    for (CamundaInputParameter camundaInputOutput : inputOutput.getCamundaInputParameters()) {
      configMap.put(camundaInputOutput.getCamundaName(), camundaInputOutput.getTextContent());
    }
    return configMap;
  }
}
