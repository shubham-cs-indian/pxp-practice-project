package com.cs.core.runtime.strategy.cammunda.broadcast;

import com.cs.core.runtime.interactor.exception.camunda.WorkflowEngineException;
import com.cs.core.runtime.interactor.model.camunda.CamundaProcessInstanceModel;
import com.cs.core.runtime.interactor.model.camunda.ICamundaExecuteProcessModel;
import com.cs.core.runtime.interactor.model.camunda.ICamundaProcessInstanceModel;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("executeProcessStrategy")
public class ExecuteProcessStrategy extends AbstractCamundaStrategy
    implements IExecuteProcessStrategy {
  
  @Autowired
  RuntimeService runtimeService;
  
  @Override
  public ICamundaProcessInstanceModel execute(ICamundaExecuteProcessModel model) throws Exception
  {
    try {
      ICamundaProcessInstanceModel camundaProcessInstanceModel = new CamundaProcessInstanceModel();
      Map<String, Object> parameters = model.getEventParameters();
      if (model.getKlassInstanceId() != null) {
        parameters.put("klassInstanceId", model.getKlassInstanceId());
      }
      String processDefinationId = model.getProcessDefinationId();
      if (processDefinationId != null) {
        ProcessInstance processInstance = runtimeService
            .startProcessInstanceById(model.getProcessDefinationId(), parameters);
        camundaProcessInstanceModel.setProcessInstanceId(processInstance.getProcessInstanceId());
      }
      return camundaProcessInstanceModel;
    }
    catch (ProcessEngineException e) {
      throw new WorkflowEngineException(e);
    }
  }
}
