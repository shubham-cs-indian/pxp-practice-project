package com.cs.core.config.strategy.usecase.tabs;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.tabs.IGetGridTabsModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetAllTabsStrategy
    extends IConfigStrategy<IConfigGetAllRequestModel, IGetGridTabsModel> {
  
}
