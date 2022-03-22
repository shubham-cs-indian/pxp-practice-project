package com.cs.core.config.strategy.usecase.themeconfiguration;

import com.cs.core.config.interactor.model.themeconfiguration.IThemeConfigurationModel;
import com.cs.core.config.interactor.model.themeconfiguration.ThemeConfigurationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class SaveThemeConfigurationStrategy extends OrientDBBaseStrategy
    implements ISaveThemeConfigurationStrategy {
  
  @Override
  public IThemeConfigurationModel execute(IThemeConfigurationModel model) throws Exception
  {
    return execute(SAVE_THEME_CONFIGURATION, model.getEntity(), ThemeConfigurationModel.class);
  }
}
