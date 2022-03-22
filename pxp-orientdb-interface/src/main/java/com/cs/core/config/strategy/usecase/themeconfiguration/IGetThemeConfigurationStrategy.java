package com.cs.core.config.strategy.usecase.themeconfiguration;

import com.cs.core.config.interactor.model.themeconfiguration.IThemeConfigurationModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetThemeConfigurationStrategy
    extends IConfigStrategy<IModel, IThemeConfigurationModel> {
  
}
