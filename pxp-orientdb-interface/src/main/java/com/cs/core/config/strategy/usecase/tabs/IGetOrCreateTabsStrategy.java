package com.cs.core.config.strategy.usecase.tabs;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tabs.ICreateTabModel;
import com.cs.core.config.interactor.model.tabs.ITabModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetOrCreateTabsStrategy
    extends IConfigStrategy<IListModel<ICreateTabModel>, IListModel<ITabModel>> {
  
}
