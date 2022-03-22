package com.cs.core.config.propertycollection;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.propertycollection.IGetPropertyCollectionModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetPropertyCollectionService extends IGetConfigService<IIdParameterModel, IGetPropertyCollectionModel> {
  
}
