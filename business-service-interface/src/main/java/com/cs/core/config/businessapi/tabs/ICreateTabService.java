package com.cs.core.config.businessapi.tabs;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.tabs.ICreateTabModel;
import com.cs.core.config.interactor.model.tabs.IGetTabModel;

public interface ICreateTabService extends ICreateConfigService<ICreateTabModel, IGetTabModel> {
  
}
