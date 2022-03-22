package com.cs.core.config.strategy.usecase.migration;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IMigrationModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.strategy.usecase.migration.IMigrationStrategy;
import org.springframework.stereotype.Component;

@Component
public class ConfigMigrationStrategy extends OrientDBBaseStrategy implements IMigrationStrategy {
  
  @Override
  public IIdParameterModel execute(IMigrationModel model) throws Exception
  {
    return execute(model.getPluginName(), model.getEntity(), IdParameterModel.class);
  }
}
