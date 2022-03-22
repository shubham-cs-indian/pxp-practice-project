package com.cs.core.config.interactor.usecase.condition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.condition.IConditionModel;
import com.cs.core.config.interactor.model.condition.IConditionResponseModel;
import com.cs.core.config.strategy.usecase.condition.ICreateConditionStrategy;

@Service
public class CreateCondition
    extends AbstractCreateConfigInteractor<IConditionModel, IConditionResponseModel>
    implements ICreateCondition {
  
  @Autowired
  ICreateConditionStrategy orientCreateConditionStrategy;
  
  public IConditionResponseModel executeInternal(IConditionModel conditionModel) throws Exception
  {
    IConditionResponseModel savedConditionModel = orientCreateConditionStrategy.execute(conditionModel);
    
    return savedConditionModel;
  }
}
