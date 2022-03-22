package com.cs.core.config.interactor.usecase.viewconfiguration;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.viewconfiguration.IViewConfigurationModel;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.usecase.base.IInteractor;

public interface ISaveDefaultViewConfiguration extends ISaveConfigInteractor<IVoidModel, IViewConfigurationModel> {
  
}
