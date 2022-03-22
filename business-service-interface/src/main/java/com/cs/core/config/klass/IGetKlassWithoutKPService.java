package com.cs.core.config.klass;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetKlassWithoutKPService
    extends IGetConfigService<IIdParameterModel, IGetKlassEntityWithoutKPModel> {
  
}
