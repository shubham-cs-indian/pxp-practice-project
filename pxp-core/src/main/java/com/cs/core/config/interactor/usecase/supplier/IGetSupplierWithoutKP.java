package com.cs.core.config.interactor.usecase.supplier;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetSupplierWithoutKP
    extends IGetConfigInteractor<IIdParameterModel, IGetKlassEntityWithoutKPModel> {
  
}
