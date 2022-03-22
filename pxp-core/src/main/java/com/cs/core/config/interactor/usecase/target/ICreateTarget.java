package com.cs.core.config.interactor.usecase.target;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.target.ITargetModel;

public interface ICreateTarget
    extends ICreateConfigInteractor<ITargetModel, IGetKlassEntityWithoutKPModel> {
  
}
