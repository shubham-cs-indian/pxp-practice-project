package com.cs.core.runtime.strategy.cammunda.broadcast;

import com.cs.core.config.interactor.model.processevent.IProcessEventModel;
import com.cs.core.runtime.interactor.exception.camunda.WorkflowEngineException;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.xml.instance.DomDocument;
import org.camunda.bpm.model.xml.instance.DomElement;
import org.springframework.stereotype.Component;


@Component
public class UploadProcessToServerStrategy extends AbstractCamundaStrategy
    implements IUploadProcessToServerStrategy {
  
  @Override
  public IModel execute(IProcessEventModel process) throws Exception
  {
    try {
      BpmnModelInstance bpmnModel = convertBpmnXmlToBpmnModel(process.getProcessDefinition());
      String processName = process.getLabel();
      DomDocument document = bpmnModel.getDocument();
      DomElement root = document.getRootElement();
      DomElement processElement = parseProcessElement(root, PROCESS);
      
      ProcessDefinition processDefinition = deployProcessInCamunda(bpmnModel, processName);
      deleteOldProcessDefinition(process, processDefinition);
      
      /* Workflow Modeler is updating this process so
       * Replicating isExecutable property from xml to ProcessEvent for 
       * querying purpose in GetProcessByConfig (Workflow Config) */
      
      boolean isExecutable = Boolean
          .parseBoolean(processElement.getAttribute(IProcessEventModel.IS_EXECUTABLE));
      process.setIsExecutable(isExecutable);
    }
    catch (ProcessEngineException e) {
      throw new WorkflowEngineException(e);
    }
    return null;
  }
  
}