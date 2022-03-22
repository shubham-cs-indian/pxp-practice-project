package com.cs.core.config.interactor.usecase.system;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.system.ISystemModel;

public interface IGetPaginatedSystems
    extends IGetConfigInteractor<IConfigGetAllRequestModel, IListModel<ISystemModel>> {
  
}
