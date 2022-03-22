package com.cs.core.config.interactor.usecase.user.validation;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.user.IGetUserValidateModel;
import com.cs.core.config.interactor.model.user.IValidateUserRequestModel;

public interface IGetUserForValidation
    extends IGetConfigInteractor<IValidateUserRequestModel, IGetUserValidateModel> {
  
}
