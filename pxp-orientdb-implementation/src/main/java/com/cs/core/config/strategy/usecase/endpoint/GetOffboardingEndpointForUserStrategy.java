package com.cs.core.config.strategy.usecase.endpoint;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.offboarding.IOffBoardingTransferRequestModel;
import com.cs.core.runtime.interactor.model.offboarding.OffBoardingTransferRequestModel;
import org.springframework.stereotype.Component;

@Component("getOffboardingEndpointForUserStrategy")
public class GetOffboardingEndpointForUserStrategy extends OrientDBBaseStrategy
    implements IGetOffboardingEndpointForUserStrategy {
  
  public static final String useCase = "GetOffboardingEndpointForUser";
  
  @Override
  public IOffBoardingTransferRequestModel execute(IModel model) throws Exception
  {
    return execute(useCase, model, OffBoardingTransferRequestModel.class);
  }
}
