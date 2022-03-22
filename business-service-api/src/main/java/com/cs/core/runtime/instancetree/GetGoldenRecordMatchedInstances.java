package com.cs.core.runtime.instancetree;

import org.springframework.stereotype.Service;

import com.cs.core.runtime.goldenrecord.bucket.IGetGoldenRecordMatchedInstances;
import com.cs.core.runtime.interactor.model.goldenrecord.IGetBucketInstancesRequestModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IGetBucketInstancesResponseModel;

@Service
public class GetGoldenRecordMatchedInstances
    extends AbstractGetGoldenRecordMatchedInstances<IGetBucketInstancesRequestModel, IGetBucketInstancesResponseModel>
    implements IGetGoldenRecordMatchedInstances {
  
  @Override
  public IGetBucketInstancesResponseModel execute(IGetBucketInstancesRequestModel dataModel) throws Exception
  {
    return super.executeInternal(dataModel);
  }
}