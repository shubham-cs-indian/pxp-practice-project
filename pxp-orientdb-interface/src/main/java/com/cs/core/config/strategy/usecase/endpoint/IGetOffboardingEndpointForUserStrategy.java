package com.cs.core.config.strategy.usecase.endpoint;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.offboarding.IOffBoardingTransferRequestModel;

public interface IGetOffboardingEndpointForUserStrategy
    extends IConfigStrategy<IModel, IOffBoardingTransferRequestModel> {
  
}
