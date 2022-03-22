package com.cs.core.runtime.strategy.usecase.goldenrecord;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.goldenrecord.ConfigDetailsForGetSourceInfoForBucketResponseModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IConfigDetailsForGetSourceInfoForBucketResponseModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IRuleIdLangaugeCodesModel;

@Component
public class GetConfigDetailsForGetSourceInfoForBucketStrategy extends OrientDBBaseStrategy 
  implements IGetConfigDetailsForGetSourceInfoForBucketStrategy {
  
  @Override
  public IConfigDetailsForGetSourceInfoForBucketResponseModel execute(IRuleIdLangaugeCodesModel model)
      throws Exception
  {
    return execute(GET_CONFIG_DETAILS_FOR_GET_SOURCE_INFO_FOR_BUCKET, model, ConfigDetailsForGetSourceInfoForBucketResponseModel.class);
  }
}
