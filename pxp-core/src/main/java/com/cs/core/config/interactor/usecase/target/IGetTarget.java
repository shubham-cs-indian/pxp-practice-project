package com.cs.core.config.interactor.usecase.target;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.target.ITargetModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetTarget extends IGetConfigInteractor<IIdParameterModel, ITargetModel> {
  
}
