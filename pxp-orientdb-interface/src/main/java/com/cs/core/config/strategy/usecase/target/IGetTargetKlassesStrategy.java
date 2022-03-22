package com.cs.core.config.strategy.usecase.target;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.targetinstance.IGetTargetKlassesModel;
import com.cs.core.runtime.interactor.model.targetinstance.IGetTargetKlassesStrategyModel;

public interface IGetTargetKlassesStrategy
    extends IConfigStrategy<IGetTargetKlassesModel, IGetTargetKlassesStrategyModel> {
  
}
