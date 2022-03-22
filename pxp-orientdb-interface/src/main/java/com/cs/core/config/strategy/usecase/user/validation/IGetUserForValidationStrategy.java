package com.cs.core.config.strategy.usecase.user.validation;

import com.cs.core.config.interactor.model.user.IGetUserValidateModel;
import com.cs.core.config.interactor.model.user.IValidateUserRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetUserForValidationStrategy
    extends IConfigStrategy<IValidateUserRequestModel, IGetUserValidateModel> {
  
}
