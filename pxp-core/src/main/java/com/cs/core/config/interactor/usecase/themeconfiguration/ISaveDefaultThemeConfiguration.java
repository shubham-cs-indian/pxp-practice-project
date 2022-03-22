package com.cs.core.config.interactor.usecase.themeconfiguration;

import com.cs.core.config.interactor.model.themeconfiguration.IThemeConfigurationModel;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.usecase.base.IInteractor;

public interface ISaveDefaultThemeConfiguration
    extends IInteractor<IVoidModel, IThemeConfigurationModel> {
  
}
