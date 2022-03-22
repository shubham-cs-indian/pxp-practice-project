package com.cs.core.config.interactor.usecase.migration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractDeleteConfigInteractor;
import com.cs.core.config.interactor.model.migration.IMigrateDeprecateVirtualCatalogModel;
import com.cs.core.config.migration.IMigrateDeprecateVirtualCatalogService;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;

@Service
public class MigrateDeprecateVirtualCatalog extends AbstractDeleteConfigInteractor<IVoidModel, IMigrateDeprecateVirtualCatalogModel> implements IMigrateDeprecateVirtualCatalog {

  @Autowired
  private IMigrateDeprecateVirtualCatalogService migratedeprecateVirtualCatalogService;
  
  @Override
  protected IMigrateDeprecateVirtualCatalogModel executeInternal(IVoidModel model) throws Exception
  {
    return migratedeprecateVirtualCatalogService.execute(new VoidModel());
  }
  
}
