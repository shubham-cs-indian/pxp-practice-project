package com.cs.core.config.propertycollection;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.propertycollection.IGetAllPropertyCollectionRequestModel;
import com.cs.core.config.interactor.model.propertycollection.IGetAllPropertyCollectionResponseModel;

public interface IGetAllPropertyCollectionService
    extends IGetConfigService<IGetAllPropertyCollectionRequestModel, IGetAllPropertyCollectionResponseModel> {
  
}
