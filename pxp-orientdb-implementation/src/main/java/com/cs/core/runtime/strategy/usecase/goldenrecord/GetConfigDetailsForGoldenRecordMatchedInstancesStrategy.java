package com.cs.core.runtime.strategy.usecase.goldenrecord;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.goldenrecord.IGetConfigDetailsForMatchedInstancesRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.goldenrecord.GetBucketInstancesResponseModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IGetBucketInstancesResponseModel;

@Component
public class GetConfigDetailsForGoldenRecordMatchedInstancesStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsForGoldenRecordMatchedInstancesStrategy {
  
  @Override
  public IGetBucketInstancesResponseModel execute(IGetConfigDetailsForMatchedInstancesRequestModel model) throws Exception
  {
    return execute(GET_CONFIG_DETAILS_FOR_GOLDEN_RECORD_MATCHED_RECORD_INSTANCES, model, GetBucketInstancesResponseModel.class);
  }
}
