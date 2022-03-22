package com.cs.core.config.strategy.usecase.tabs;

import com.cs.core.config.interactor.model.tabs.ICreateTabModel;
import com.cs.core.config.interactor.model.tabs.IGetTabModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ICreateTabStrategy extends IConfigStrategy<ICreateTabModel, IGetTabModel> {
  
}
