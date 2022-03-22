package com.cs.core.config.strategy.usecase.system;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.system.ISystemModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetPaginatedSystemsStrategy
    extends IConfigStrategy<IConfigGetAllRequestModel, IListModel<ISystemModel>> {
  
}
