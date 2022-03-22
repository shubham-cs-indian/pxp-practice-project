package com.cs.core.runtime.goldenrecord.bucket;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.goldenrecord.IGetBucketInstancesRequestModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IGetBucketInstancesResponseModel;

public interface IGetGoldenRecordMatchedInstances extends
  IRuntimeService<IGetBucketInstancesRequestModel, IGetBucketInstancesResponseModel> {
  
}
