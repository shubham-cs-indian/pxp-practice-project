package com.cs.core.config.asset;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;

public interface ICreateAssetService
    extends ICreateConfigService<IAssetModel, IGetKlassEntityWithoutKPModel> {
  
}
