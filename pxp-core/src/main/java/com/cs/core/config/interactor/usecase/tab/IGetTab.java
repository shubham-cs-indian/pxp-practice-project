package com.cs.core.config.interactor.usecase.tab;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.tabs.IGetTabModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetTab extends IGetConfigInteractor<IIdParameterModel, IGetTabModel> {
  
}
