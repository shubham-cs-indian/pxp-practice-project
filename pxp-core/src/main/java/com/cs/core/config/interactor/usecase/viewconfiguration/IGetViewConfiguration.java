package com.cs.core.config.interactor.usecase.viewconfiguration;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.viewconfiguration.IViewConfigurationModel;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;

public interface IGetViewConfiguration extends IGetConfigInteractor<IVoidModel, IViewConfigurationModel> {
  
}
