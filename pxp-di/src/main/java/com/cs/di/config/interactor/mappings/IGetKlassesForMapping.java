package com.cs.di.config.interactor.mappings;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.mapping.IGetKlassesForMappingModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetKlassesForMapping
    extends IGetConfigInteractor<IIdParameterModel, IGetKlassesForMappingModel> {
  
}
