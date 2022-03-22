package com.cs.workflow.camunda;

import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.FlowElement;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.springframework.stereotype.Component;

import com.cs.workflow.base.WorkflowConstants;

@Component("camundaTask")
public class CamundaTask extends AbstractCamundaReaderWriter implements JavaDelegate {
  
  @Override
  public void execute(DelegateExecution execution) throws Exception
  {
    FlowElement flowElement = (FlowElement) ((DelegateExecution) execution).getBpmnModelElementInstance();
    if (flowElement instanceof StartEvent) {
      tasksFactory.getStartEvent().execute(execution, this);
    }
    else if (flowElement instanceof EndEvent) {
      tasksFactory.getEndEvent().execute(execution, this);
    }
    else if (flowElement instanceof ServiceTask) {
      tasksFactory.getTask((String) getVariable(execution, WorkflowConstants.TASK_ID)).execute(execution, this);
    }
  }

  @Override
  public String getProcessInstanceId(Object execution)
  {
    return ((DelegateExecution) execution).getProcessInstanceId();
  }

  @Override
  public String getProcessDefinitionId(Object execution)
  {
    return ((DelegateExecution) execution).getProcessDefinitionId();
  }

  @Override
  public String getActivityInstanceId(Object execution)
  {
    return ((DelegateExecution) execution).getActivityInstanceId();

  }

  @Override
  public String getId(Object execution)
  {
    return ((DelegateExecution) execution).getId();
  }

  @Override
  public String getActivityName(Object execution)
  {
    return ((DelegateExecution) execution).getCurrentActivityName();
  }

  @Override
  public List<Map<String, Object>> getFormFields(Object execution)
  {
    // TODO Auto-generated method stub
    return null;
  }

}
