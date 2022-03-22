package com.cs.core.config.propertycollection;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.propertycollection.IGetPropertyCollectionModel;
import com.cs.core.config.interactor.model.propertycollection.ISavePropertyCollectionModel;

public interface ISavePropertyCollectionService
    extends ISaveConfigService<ISavePropertyCollectionModel, IGetPropertyCollectionModel> {
  
}
