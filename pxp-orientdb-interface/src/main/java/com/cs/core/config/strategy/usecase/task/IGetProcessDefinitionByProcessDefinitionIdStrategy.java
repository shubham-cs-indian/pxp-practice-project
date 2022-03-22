package com.cs.core.config.strategy.usecase.task;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.camunda.IGetCamundaProcessDefinitionResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IGetProcessDefinitionByProcessDefinitionIdStrategy
    extends IConfigStrategy<IIdsListParameterModel, IGetCamundaProcessDefinitionResponseModel> {
  
}
