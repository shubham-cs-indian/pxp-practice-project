package com.cs.core.config.strategy.usecase.themeconfiguration;

import com.cs.core.config.interactor.model.themeconfiguration.IThemeConfigurationModel;
import com.cs.core.config.interactor.model.themeconfiguration.ThemeConfigurationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import org.springframework.stereotype.Component;

@Component
public class GetThemeConfigurationStrategy extends OrientDBBaseStrategy
    implements IGetThemeConfigurationStrategy {
  
  @Override
  public IThemeConfigurationModel execute(IModel model) throws Exception
  {
    return execute(GET_THEME_CONFIGURATION, model, ThemeConfigurationModel.class);
  }
  
  @Override
  public String getUsecase()
  {
    return "Get Theme Configuration Strategy";
  }
}
