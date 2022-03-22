package com.cs.core.config.strategy.usecase.system;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.system.ICreateSystemModel;
import com.cs.core.config.interactor.model.system.ISystemModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetOrCreateSystemsStrategy
    extends IConfigStrategy<IListModel<ICreateSystemModel>, IListModel<ISystemModel>> {
  
}
