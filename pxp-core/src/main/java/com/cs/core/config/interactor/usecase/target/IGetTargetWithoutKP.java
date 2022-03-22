package com.cs.core.config.interactor.usecase.target;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetTargetWithoutKP
    extends IGetConfigInteractor<IIdParameterModel, IGetKlassEntityWithoutKPModel> {
  
}
