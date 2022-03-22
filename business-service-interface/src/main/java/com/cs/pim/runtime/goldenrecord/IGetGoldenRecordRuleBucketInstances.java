package com.cs.pim.runtime.goldenrecord;

import com.cs.core.config.interactor.model.instancetree.IGetInstanceTreeRequestModel;
import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.instancetree.IGetInstanceTreeForGoldenRecordBucketResponseModel;

public interface IGetGoldenRecordRuleBucketInstances extends
IRuntimeService<IGetInstanceTreeRequestModel, IGetInstanceTreeForGoldenRecordBucketResponseModel> {
  
}