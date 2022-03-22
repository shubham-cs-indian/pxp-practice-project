package com.cs.core.config.asset;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetAssetWithoutKPService
    extends IGetConfigService<IIdParameterModel, IGetKlassEntityWithoutKPModel> {
  
}
