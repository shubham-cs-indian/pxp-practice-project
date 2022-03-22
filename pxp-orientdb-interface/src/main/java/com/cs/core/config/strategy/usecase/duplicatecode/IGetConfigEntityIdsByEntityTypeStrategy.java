package com.cs.core.config.strategy.usecase.duplicatecode;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IGetEntityIdsByEntityTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IGetConfigEntityIdsByEntityTypeStrategy
    extends IConfigStrategy<IGetEntityIdsByEntityTypeModel, IIdsListParameterModel> {
  
}
