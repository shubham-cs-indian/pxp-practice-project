package com.cs.core.config.strategy.usecase.tabs;

import com.cs.core.config.interactor.model.tabs.IGetTabModel;
import com.cs.core.config.interactor.model.tabs.ISaveTabModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ISaveTabStrategy extends IConfigStrategy<ISaveTabModel, IGetTabModel> {
  
}
