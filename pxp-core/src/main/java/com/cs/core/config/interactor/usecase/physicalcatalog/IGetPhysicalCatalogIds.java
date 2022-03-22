package com.cs.core.config.interactor.usecase.physicalcatalog;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.physicalcatalog.IGetPhysicalCatalogIdsModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetPhysicalCatalogIds
    extends IGetConfigInteractor<IModel, IGetPhysicalCatalogIdsModel> {
  
}
