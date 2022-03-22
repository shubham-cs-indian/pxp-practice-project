package com.cs.core.config.interactor.usecase.condition;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.condition.IConditionModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetCondition extends IGetConfigInteractor<IIdParameterModel, IConditionModel> {
  
}
