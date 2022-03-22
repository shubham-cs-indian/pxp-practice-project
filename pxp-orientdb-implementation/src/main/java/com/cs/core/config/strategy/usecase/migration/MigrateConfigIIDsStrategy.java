package com.cs.core.config.strategy.usecase.migration;

import com.cs.core.config.interactor.model.migration.IMigrateConfigIIDsModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import org.springframework.stereotype.Component;

/**
 * @author tauseef
 */
@Component
public class MigrateConfigIIDsStrategy extends OrientDBBaseStrategy
    implements IMigrateConfigIIDsStrategy {

  @Override
  public IVoidModel execute(IMigrateConfigIIDsModel model) throws Exception {
    return execute("MigrateConfigIIDs", model, VoidModel.class);
  }
}
