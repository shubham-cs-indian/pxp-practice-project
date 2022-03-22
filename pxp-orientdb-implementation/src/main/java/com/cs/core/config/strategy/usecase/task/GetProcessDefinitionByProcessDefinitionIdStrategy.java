package com.cs.core.config.strategy.usecase.task;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.camunda.GetCamundaProcessDefinitionResponseModel;
import com.cs.core.runtime.interactor.model.camunda.IGetCamundaProcessDefinitionResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Component
public class GetProcessDefinitionByProcessDefinitionIdStrategy extends OrientDBBaseStrategy
    implements IGetProcessDefinitionByProcessDefinitionIdStrategy {
  
  @Override
  public IGetCamundaProcessDefinitionResponseModel execute(IIdsListParameterModel model) throws Exception
  {
    return execute(GET_PROCESS_DEFINITION_BY_PROCESS_DEFINITION_ID, model, GetCamundaProcessDefinitionResponseModel.class);
  }
}

