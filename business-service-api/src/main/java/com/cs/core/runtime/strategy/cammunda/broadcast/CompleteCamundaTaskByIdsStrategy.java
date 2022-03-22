package com.cs.core.runtime.strategy.cammunda.broadcast;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.entity.taskinstance.ICamundaFormField;
import com.cs.core.runtime.interactor.exception.camunda.WorkflowEngineException;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.taskinstance.IGetTaskInstanceModel;
import com.cs.workflow.camunda.IBPMNEngineService;

import org.camunda.bpm.engine.ProcessEngineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CompleteCamundaTaskByIdsStrategy extends AbstractCamundaStrategy implements ICompleteCamundaTaskByIdsStrategy {
  
  @Autowired
  protected IBPMNEngineService bpmnService;
  
  @Override
  public IModel execute(IListModel<IGetTaskInstanceModel> models) throws Exception
  {
    try {
      for (IGetTaskInstanceModel model : models.getList()) {
        List<ICamundaFormField> formFields = model.getFormFields();
        Map<String, Object> variables = new HashMap<>();
        for (ICamundaFormField iCamundaFormField : formFields) {
          Map<String, Object> value = iCamundaFormField.getValue();
          variables.put(iCamundaFormField.getId(), value.get("value"));
        }
        bpmnService.notifyTask(model.getCamundaTaskInstanceId(), variables);
      }
      return null;
    }
    catch (ProcessEngineException e) {
      throw new WorkflowEngineException(e);
    }
  }
  
}