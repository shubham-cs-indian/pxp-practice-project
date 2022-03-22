package com.cs.core.config.interactor.model.processevent;

import com.cs.core.config.strategy.usecase.task.IGetProcessDefinitionByProcessDefinitionIdStrategy;
import com.cs.core.runtime.interactor.exception.camunda.WorkflowEngineException;
import com.cs.core.runtime.interactor.model.camunda.CamundaProcessModel;
import com.cs.core.runtime.interactor.model.camunda.ICamundaProcessModel;
import com.cs.core.runtime.interactor.model.camunda.IGetCamundaProcessDefinationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionDetailModel;
import com.cs.core.runtime.strategy.cammunda.broadcast.AbstractCamundaStrategy;
import com.cs.core.runtime.strategy.cammunda.broadcast.IGetCamundaProcessDefinationStrategy;
import com.cs.workflow.camunda.CamundaProcessUtils;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.exception.NullValueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;

@Component("getCamundaProcessDefinationStrategy")
public class GetCamundaProcessDefinationStrategy extends AbstractCamundaStrategy implements IGetCamundaProcessDefinationStrategy {
  
  @Autowired
  protected CamundaProcessUtils                                camundaProcessUtils;
  
  @Autowired
  protected IGetProcessDefinitionByProcessDefinitionIdStrategy getProcessDefinitionByProcessDefinitionIdStrategy;
  
  @Override
  public ICamundaProcessModel execute(IGetCamundaProcessDefinationModel model) throws Exception
  {
    try {
      String processDefinationId = model.getProcessDefinationId();
      String processInstanceId = model.getProcessInstanceId();
      IIdsListParameterModel ids = new IdsListParameterModel();
      ids.setIds(Arrays.asList(processDefinationId));
      
      String processDefination = (String) getProcessDefinitionByProcessDefinitionIdStrategy.execute(ids).getProcessDefinition()
          .get(processDefinationId);
      ICamundaProcessModel responseModel = new CamundaProcessModel();
      responseModel.setProcessDefinationId(processDefinationId);
      responseModel.setProcessDefination(processDefination);
      
      ArrayList<String> completedActivityIds = new ArrayList<String>();
      ArrayList<String> currentActivityIds = new ArrayList<String>();
      
      camundaProcessUtils.getCompletedActivity(processInstanceId, completedActivityIds);
      camundaProcessUtils.getCurrentActivity(processInstanceId, currentActivityIds);
      
      responseModel.setCurrentActivityIds(currentActivityIds);
      responseModel.setCompletedActivityIds(completedActivityIds);
      return responseModel;
    }
    catch (ProcessEngineException e) {
      WorkflowEngineException exception = new WorkflowEngineException(e);
      if (e instanceof NullValueException && e.getMessage().contains("no deployed process definition found with id '")) {
        if (!exception.getExceptionDetails().isEmpty()) {
          IExceptionDetailModel exceptionDetail = exception.getExceptionDetails().get(0);
          exceptionDetail.setKey("BPMN_Process_Defination_not_found");
        }
      }
      throw exception;
    }
  }
  
}