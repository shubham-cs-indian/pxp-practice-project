package com.cs.core.config.interactor.usecase.textasset;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.textasset.ITextAssetModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetTextAsset extends IGetConfigInteractor<IIdParameterModel, ITextAssetModel> {
  
}
