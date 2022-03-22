package com.cs.core.config.strategy.usecase.themeconfiguration;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.themeconfiguration.IThemeConfigurationModel;
import com.cs.core.config.interactor.model.themeconfiguration.ThemeConfigurationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component
public class SaveDefaultThemeConfigurationStrategy extends OrientDBBaseStrategy
    implements ISaveDefaultThemeConfigurationStrategy {
  
  @Override
  public IThemeConfigurationModel execute(IThemeConfigurationModel model) throws Exception
  {
    return execute(SAVE_DEFAULT_THEME_CONFIGURATION, model,
        ThemeConfigurationModel.class);
  }
}
