package com.cs.core.config.strategy.usecase.migration;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.migration.IMigrateDeprecateVirtualCatalogModel;
import com.cs.core.config.interactor.model.migration.MigrateDeprecateVirtualCatalogModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;

@Component
public class MigrateDeprecateVirtualCatalogStrategy extends OrientDBBaseStrategy implements IMigrateDeprecateVirtualCatalogStrategy {
  
  
  @Override
  public IMigrateDeprecateVirtualCatalogModel execute(IVoidModel model) throws Exception
  {
    return execute(MIGRATE_DEPRECATE_VIRTUAL_CATALOG, model, MigrateDeprecateVirtualCatalogModel.class);
  }
}
