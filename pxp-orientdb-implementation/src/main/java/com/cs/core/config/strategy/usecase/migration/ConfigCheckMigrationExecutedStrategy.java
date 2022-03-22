package com.cs.core.config.strategy.usecase.migration;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.strategy.usecase.migration.IMigrationExecutedStrategy;
import org.springframework.stereotype.Component;

@Component
public class ConfigCheckMigrationExecutedStrategy extends OrientDBBaseStrategy
    implements IMigrationExecutedStrategy {
  
  @Override
  public IIdsListParameterModel execute(IIdsListParameterModel model) throws Exception
  {
    return execute(CHECK_MIGRATION_EXECUTED, model, IdsListParameterModel.class);
  }
}
