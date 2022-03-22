package com.cs.api.estordbmsmigration.strategy.migration;

import org.springframework.stereotype.Component;

import com.cs.api.estordbmsmigration.model.migration.ISyncIIDToODBModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;

@Component
public class SyncIIDToODBStrategy extends OrientDBBaseStrategy implements ISyncIIDToODBStrategy {
  
  @Override
  public IVoidModel execute(ISyncIIDToODBModel model) throws Exception
  {
    return execute("SyncIIDToODB", model, VoidModel.class);
  }
  
}
