package com.cs.core.config.strategy.usecase.user.validation;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.user.GetUserValidateModel;
import com.cs.core.config.interactor.model.user.IGetUserValidateModel;
import com.cs.core.config.interactor.model.user.IValidateUserRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component
public class GetUserForValidationStrategy extends OrientDBBaseStrategy
    implements IGetUserForValidationStrategy {
  
  @Override
  public IGetUserValidateModel execute(IValidateUserRequestModel model) throws Exception
  {
    return execute(GET_VALIDATE_USER, model, GetUserValidateModel.class);
  }
}
