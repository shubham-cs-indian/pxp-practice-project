package com.cs.core.config.interactor.usecase.themeconfiguration;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.themeconfiguration.IThemeConfigurationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetThemeConfiguration
    extends IGetConfigInteractor<IModel, IThemeConfigurationModel> {
  
}
