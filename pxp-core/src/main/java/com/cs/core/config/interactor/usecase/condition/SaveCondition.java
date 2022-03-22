package com.cs.core.config.interactor.usecase.condition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.condition.IConditionModel;
import com.cs.core.config.interactor.model.condition.IConditionResponseModel;
import com.cs.core.config.strategy.usecase.condition.ISaveConditionStrategy;

@Service
public class SaveCondition
    extends AbstractSaveConfigInteractor<IConditionModel, IConditionResponseModel>
    implements ISaveCondition {
  
  @Autowired
  ISaveConditionStrategy orientSaveConditionStrategy;
  
  @Override
  public IConditionResponseModel executeInternal(IConditionModel conditionModel) throws Exception
  {
    
    return orientSaveConditionStrategy.execute(conditionModel);
  }
}
