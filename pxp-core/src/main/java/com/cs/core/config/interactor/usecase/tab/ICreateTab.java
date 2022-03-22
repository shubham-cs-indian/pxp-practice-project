package com.cs.core.config.interactor.usecase.tab;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.tabs.ICreateTabModel;
import com.cs.core.config.interactor.model.tabs.IGetTabModel;

public interface ICreateTab extends ICreateConfigInteractor<ICreateTabModel, IGetTabModel> {
  
}
