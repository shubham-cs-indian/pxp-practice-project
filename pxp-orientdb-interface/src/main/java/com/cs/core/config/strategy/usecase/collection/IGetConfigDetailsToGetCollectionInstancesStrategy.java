package com.cs.core.config.strategy.usecase.collection;

import com.cs.core.runtime.interactor.model.collections.IConfigDetailsToGetCollectionInstancesRequestModel;
import com.cs.core.runtime.interactor.model.collections.IConfigDetailsToGetCollectionInstancesResponseModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetConfigDetailsToGetCollectionInstancesStrategy extends
    IRuntimeStrategy<IConfigDetailsToGetCollectionInstancesRequestModel, IConfigDetailsToGetCollectionInstancesResponseModel> {
  
}
