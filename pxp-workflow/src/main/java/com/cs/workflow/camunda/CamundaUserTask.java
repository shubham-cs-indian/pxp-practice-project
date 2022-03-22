package com.cs.workflow.camunda;

import com.cs.workflow.base.WorkflowConstants;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("camundaUserTask")
public class CamundaUserTask extends AbstractCamundaReaderWriter implements TaskListener {
  
  @Autowired
  public FormService formService;

    @Override
    public void notify(DelegateTask execution) {
        try {
            tasksFactory.getTask((String) getVariable(execution, WorkflowConstants.TASK_ID)).execute(execution, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getProcessInstanceId(Object execution)
    {
        return ((DelegateTask) execution).getProcessInstanceId();
    }

    @Override
    public String getProcessDefinitionId(Object execution)
    {
        return ((DelegateTask) execution).getProcessDefinitionId();
    }

    @Override
    public String getActivityInstanceId(Object execution)
    {
        return this.getId(execution);
    }

    @Override
    public List<Map<String, Object>> getFormFields(Object execution)
    {
      String id = ((DelegateTask) execution).getId();
    
      return formService.getTaskFormData(id).getFormFields().stream().map(formField->{
        Map<String,Object> formdata = new HashMap<String,Object>();    
        formdata.put("id", formField.getId());
        formdata.put("label",formField.getLabel());
        formdata.put("value",formField.getValue());
        formdata.put("properties",formField.getProperties());
        formdata.put("type", formField.getType());
                               
        return formdata;
      }).collect(Collectors.toList());
    }
    
    @Override
    public String getId(Object execution)
    {
        return ((DelegateTask) execution).getId();
    }

    @Override
    public String getActivityName(Object execution)
    {
        return ((DelegateTask) execution).getName();
    }
}
