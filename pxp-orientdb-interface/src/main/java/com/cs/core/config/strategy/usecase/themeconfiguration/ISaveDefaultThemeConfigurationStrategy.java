package com.cs.core.config.strategy.usecase.themeconfiguration;

import com.cs.core.config.interactor.model.themeconfiguration.IThemeConfigurationModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ISaveDefaultThemeConfigurationStrategy
    extends IConfigStrategy<IThemeConfigurationModel, IThemeConfigurationModel> {
  
}
