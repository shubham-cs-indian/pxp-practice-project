package com.cs.core.config.strategy.usecase.textasset;

import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.textasset.ITextAssetModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ICreateTextAssetStrategy
    extends IConfigStrategy<ITextAssetModel, IGetKlassEntityWithoutKPModel> {
  
}
