package com.cs.core.config.strategy.usecase.migration;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;

@Component
public class MigrateDefaultTranslationsStrategy extends OrientDBBaseStrategy
    implements IMigrateDefaultTranslationsStrategy {

  @Override
  public IVoidModel execute(IIdParameterModel model) throws Exception {    
    return execute("OrientMigrationForDefaultTranslations", model, VoidModel.class);
  }
}
