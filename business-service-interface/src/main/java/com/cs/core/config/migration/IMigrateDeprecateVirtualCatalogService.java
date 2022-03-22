package com.cs.core.config.migration;

import com.cs.config.businessapi.base.IDeleteConfigService;
import com.cs.core.config.interactor.model.migration.IMigrateDeprecateVirtualCatalogModel;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;

public interface IMigrateDeprecateVirtualCatalogService  extends IDeleteConfigService<IVoidModel, IMigrateDeprecateVirtualCatalogModel>{
  
}
