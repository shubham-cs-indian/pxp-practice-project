package com.cs.core.config.strategy.usecase.target;

import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPStrategyResponseModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ISaveTargetStrategy
    extends IConfigStrategy<IKlassSaveModel, IGetKlassEntityWithoutKPStrategyResponseModel> {
  
}
