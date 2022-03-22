package com.cs.core.config.businessapi.tabs;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.tabs.IGetGridTabsModel;

public interface IGetAllTabsService extends IGetConfigService<IConfigGetAllRequestModel, IGetGridTabsModel> {
  
}
