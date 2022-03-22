package com.cs.core.config.interactor.usecase.migration;

import com.cs.config.interactor.usecase.base.IDeleteConfigInteractor;
import com.cs.core.config.interactor.model.migration.IMigrateDeprecateVirtualCatalogModel;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;

public interface IMigrateDeprecateVirtualCatalog extends IDeleteConfigInteractor<IVoidModel, IMigrateDeprecateVirtualCatalogModel> {
  
}
