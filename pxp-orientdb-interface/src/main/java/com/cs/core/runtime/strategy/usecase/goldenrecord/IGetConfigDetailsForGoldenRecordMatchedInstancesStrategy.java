package com.cs.core.runtime.strategy.usecase.goldenrecord;

import com.cs.core.config.interactor.model.goldenrecord.IGetConfigDetailsForMatchedInstancesRequestModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IGetBucketInstancesResponseModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetConfigDetailsForGoldenRecordMatchedInstancesStrategy
    extends IRuntimeStrategy<IGetConfigDetailsForMatchedInstancesRequestModel, IGetBucketInstancesResponseModel> {
  
}
