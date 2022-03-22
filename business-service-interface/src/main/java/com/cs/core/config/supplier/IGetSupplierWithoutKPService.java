package com.cs.core.config.supplier;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetSupplierWithoutKPService
    extends IGetConfigService<IIdParameterModel, IGetKlassEntityWithoutKPModel> {
  
}
