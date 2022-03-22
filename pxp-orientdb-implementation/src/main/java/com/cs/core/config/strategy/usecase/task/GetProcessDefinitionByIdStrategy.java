package com.cs.core.config.strategy.usecase.task;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.camunda.GetCamundaProcessDefinitionResponseModel;
import com.cs.core.runtime.interactor.model.camunda.IGetCamundaProcessDefinitionResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import org.springframework.stereotype.Component;

@Component
public class GetProcessDefinitionByIdStrategy extends OrientDBBaseStrategy
    implements IGetProcessDefinitionByIdStrategy {
  
  @Override
  public IGetCamundaProcessDefinitionResponseModel execute(IIdsListParameterModel model)
      throws Exception
  {
    return execute(GET_PROCESS_DEFINITION_BY_ID, model,
        GetCamundaProcessDefinitionResponseModel.class);
  }
}
