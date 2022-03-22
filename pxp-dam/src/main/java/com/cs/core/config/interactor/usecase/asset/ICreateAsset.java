package com.cs.core.config.interactor.usecase.asset;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;

public interface ICreateAsset
    extends ICreateConfigInteractor<IAssetModel, IGetKlassEntityWithoutKPModel> {
  
}
