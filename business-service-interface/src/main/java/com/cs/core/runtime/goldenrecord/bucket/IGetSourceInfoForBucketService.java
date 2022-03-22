package com.cs.core.runtime.goldenrecord.bucket;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.goldenrecord.IGetSourceInfoForBucketResponseModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IIdBucketIdModel;

public interface IGetSourceInfoForBucketService extends
IRuntimeService<IIdBucketIdModel, IGetSourceInfoForBucketResponseModel> {
  
}
