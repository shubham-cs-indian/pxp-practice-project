package com.cs.core.config.propertycollection;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.propertycollection.IGetPropertyCollectionModel;
import com.cs.core.config.interactor.model.propertycollection.IPropertyCollectionModel;

public interface ICreatePropertyCollectionService extends ICreateConfigService<IPropertyCollectionModel, IGetPropertyCollectionModel> {
  
}
