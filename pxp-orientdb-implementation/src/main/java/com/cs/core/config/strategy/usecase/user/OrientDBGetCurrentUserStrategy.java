package com.cs.core.config.strategy.usecase.user;

import com.cs.core.config.interactor.model.user.GetCurrentUserModel;
import com.cs.core.config.interactor.model.user.IGetCurrentUserModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

@Component("getCurrentUserStrategy")
public class OrientDBGetCurrentUserStrategy extends OrientDBBaseStrategy
    implements IGetCurrentUserStrategy {
  
  public static final String useCase = "GetCurrentUser";
  
  @Override
  public IGetCurrentUserModel execute(IIdParameterModel model) throws Exception
  {
    return execute(useCase, model, GetCurrentUserModel.class);
  }
  
  @Override
  public String getUsecase()
  {
    return "Get Current User Strategy";
  }
}
