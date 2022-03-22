package com.cs.core.config.strategy.usecase.user;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.user.GetGridUsersResponseModel;
import com.cs.core.config.interactor.model.user.IGetGridUsersResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetGridUsersStrategy extends OrientDBBaseStrategy implements IGetGridUsersStrategy {
  
  @Override
  public IGetGridUsersResponseModel execute(IConfigGetAllRequestModel model) throws Exception
  {
    return execute(GET_GRID_USERS, model, GetGridUsersResponseModel.class);
  }
}
