package com.cs.core.config.interactor.usecase.tab;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.tabs.IGetGridTabsModel;

public interface IGetAllTabs
    extends IGetConfigInteractor<IConfigGetAllRequestModel, IGetGridTabsModel> {
  
}
