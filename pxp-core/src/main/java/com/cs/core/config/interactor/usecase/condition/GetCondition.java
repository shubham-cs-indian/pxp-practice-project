package com.cs.core.config.interactor.usecase.condition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.condition.IConditionModel;
import com.cs.core.config.strategy.usecase.condition.IGetConditionStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetCondition extends AbstractGetConfigInteractor<IIdParameterModel, IConditionModel>
    implements IGetCondition {
  
  @Autowired
  IGetConditionStrategy getConditionStrategy;
  
  @Override
  public IConditionModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getConditionStrategy.execute(dataModel);
  }
}
