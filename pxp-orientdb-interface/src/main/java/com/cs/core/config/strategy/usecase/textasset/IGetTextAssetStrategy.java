package com.cs.core.config.strategy.usecase.textasset;

import com.cs.core.config.interactor.model.textasset.ITextAssetModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetTextAssetStrategy extends IConfigStrategy<IIdParameterModel, ITextAssetModel> {
  
}
