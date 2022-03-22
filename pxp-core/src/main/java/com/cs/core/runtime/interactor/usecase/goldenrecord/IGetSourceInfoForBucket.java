package com.cs.core.runtime.interactor.usecase.goldenrecord;

import com.cs.core.runtime.interactor.model.goldenrecord.IGetSourceInfoForBucketResponseModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IIdBucketIdModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetSourceInfoForBucket extends
  IRuntimeInteractor<IIdBucketIdModel, IGetSourceInfoForBucketResponseModel> {
  
}
