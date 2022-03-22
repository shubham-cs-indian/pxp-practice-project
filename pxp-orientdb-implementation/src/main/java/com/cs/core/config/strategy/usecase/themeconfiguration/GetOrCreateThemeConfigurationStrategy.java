package com.cs.core.config.strategy.usecase.themeconfiguration;

import com.cs.core.config.interactor.model.themeconfiguration.IThemeConfigurationModel;
import com.cs.core.config.interactor.model.themeconfiguration.ThemeConfigurationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetOrCreateThemeConfigurationStrategy extends OrientDBBaseStrategy
    implements IGetOrCreateThemeConfigurationStrategy {
  
  @Override
  public IThemeConfigurationModel execute(IThemeConfigurationModel model) throws Exception
  {
    return execute(GET_OR_CREATE_THEME_CONFIGURATION, model, ThemeConfigurationModel.class);
  }
}
