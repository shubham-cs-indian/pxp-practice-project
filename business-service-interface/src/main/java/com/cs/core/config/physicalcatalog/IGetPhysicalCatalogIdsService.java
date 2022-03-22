package com.cs.core.config.physicalcatalog;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.physicalcatalog.IGetPhysicalCatalogIdsModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetPhysicalCatalogIdsService
    extends IGetConfigService<IModel, IGetPhysicalCatalogIdsModel> {
  
}
