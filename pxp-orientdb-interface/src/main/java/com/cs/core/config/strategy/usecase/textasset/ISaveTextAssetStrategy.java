package com.cs.core.config.strategy.usecase.textasset;

import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPStrategyResponseModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ISaveTextAssetStrategy
    extends IConfigStrategy<IKlassSaveModel, IGetKlassEntityWithoutKPStrategyResponseModel> {
  
}
