package com.cs.core.config.interactor.usecase.textasset;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.textasset.ITextAssetModel;

public interface ICreateTextAsset
    extends ICreateConfigInteractor<ITextAssetModel, IGetKlassEntityWithoutKPModel> {
  
}
