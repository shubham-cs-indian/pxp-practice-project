package com.cs.core.runtime.interactor.usecase.goldenrecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.runtime.goldenrecord.bucket.IGetSourceInfoForBucketService;
import com.cs.core.runtime.interactor.model.goldenrecord.IGetSourceInfoForBucketResponseModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IIdBucketIdModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Component
public class GetSourceInfoForBucket extends AbstractRuntimeInteractor<IIdBucketIdModel, IGetSourceInfoForBucketResponseModel> implements IGetSourceInfoForBucket {

  @Autowired
  protected IGetSourceInfoForBucketService getSourceInfoForBucketService;
  
  @Override
  protected IGetSourceInfoForBucketResponseModel executeInternal(IIdBucketIdModel model)
      throws Exception
  {
    return getSourceInfoForBucketService.execute(model);
  }
  
}
