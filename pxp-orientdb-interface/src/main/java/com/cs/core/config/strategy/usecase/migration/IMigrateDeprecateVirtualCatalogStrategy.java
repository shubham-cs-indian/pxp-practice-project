package com.cs.core.config.strategy.usecase.migration;

import com.cs.core.config.interactor.model.migration.IMigrateDeprecateVirtualCatalogModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;

public interface IMigrateDeprecateVirtualCatalogStrategy extends IConfigStrategy<IVoidModel, IMigrateDeprecateVirtualCatalogModel> {
  
}
