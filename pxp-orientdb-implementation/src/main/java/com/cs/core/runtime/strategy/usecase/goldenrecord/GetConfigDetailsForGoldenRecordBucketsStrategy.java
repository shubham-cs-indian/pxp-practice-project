package com.cs.core.runtime.strategy.usecase.goldenrecord;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.goldenrecord.GetConfigDetailsForGoldenRecordRuleResponseModel;
import com.cs.core.config.interactor.model.goldenrecord.IGetConfigDetailsForGoldenRecordRuleResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Component
public class GetConfigDetailsForGoldenRecordBucketsStrategy  extends OrientDBBaseStrategy implements IGetConfigDetailsForGoldenRecordBucketsStrategy{

  @Override
  public IGetConfigDetailsForGoldenRecordRuleResponseModel execute(IIdsListParameterModel model)
      throws Exception
  {
    return execute(GET_CONFIG_DETAILS_FOR_GOLDEN_RECORD_BUCKETS, model, GetConfigDetailsForGoldenRecordRuleResponseModel.class);
  }
  
}
