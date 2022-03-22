package com.cs.core.config.strategy.usecase.propertycollection;

import com.cs.core.config.interactor.model.propertycollection.IGetAllPropertyCollectionRequestModel;
import com.cs.core.config.interactor.model.propertycollection.IGetAllPropertyCollectionResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetAllPropertyCollectionStrategy extends
    IConfigStrategy<IGetAllPropertyCollectionRequestModel, IGetAllPropertyCollectionResponseModel> {
  
}
