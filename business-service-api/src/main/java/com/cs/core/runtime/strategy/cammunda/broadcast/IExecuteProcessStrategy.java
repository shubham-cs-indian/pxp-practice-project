package com.cs.core.runtime.strategy.cammunda.broadcast;

import com.cs.core.runtime.interactor.model.camunda.ICamundaExecuteProcessModel;
import com.cs.core.runtime.interactor.model.camunda.ICamundaProcessInstanceModel;

public interface IExecuteProcessStrategy
    extends ICamundaStrategy<ICamundaExecuteProcessModel, ICamundaProcessInstanceModel> {
  
}
