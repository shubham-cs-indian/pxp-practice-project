package com.cs.core.config.strategy.usecase.migration;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.themeconfiguration.IThemeConfigurationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;

@Component
public class MigrationForThemeConfigurationStrategy19_1sr extends OrientDBBaseStrategy implements IMigrationForThemeConfigurationStrategy19_1SR {
  
  @Override
  public IVoidModel execute(IThemeConfigurationModel model) throws Exception
  {
    return execute(ORIENT_MIGRATION_FOR_THEME_CONFIGURATION_19_1_SR, model, VoidModel.class);
  }
  
}

