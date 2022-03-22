package com.cs.core.config.strategy.usecase.asset;

import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ICreateAssetStrategy
    extends IConfigStrategy<IAssetModel, IGetKlassEntityWithoutKPModel> {
  
}
