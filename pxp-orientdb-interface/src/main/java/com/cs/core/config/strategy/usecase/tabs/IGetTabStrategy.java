package com.cs.core.config.strategy.usecase.tabs;

import com.cs.core.config.interactor.model.tabs.IGetTabModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetTabStrategy extends IConfigStrategy<IIdParameterModel, IGetTabModel> {
  
}
