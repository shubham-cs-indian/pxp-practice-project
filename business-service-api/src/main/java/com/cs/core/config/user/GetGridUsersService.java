package com.cs.core.config.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.user.IGetGridUsersResponseModel;
import com.cs.core.config.strategy.usecase.user.IGetGridUsersStrategy;

@Service
public class GetGridUsersService
    extends AbstractGetConfigService<IConfigGetAllRequestModel, IGetGridUsersResponseModel>
    implements IGetGridUsersService {
  
  @Autowired
  protected IGetGridUsersStrategy getGridUserStrategy;
  
  @Override
  public IGetGridUsersResponseModel executeInternal(IConfigGetAllRequestModel model) throws Exception
  {
    return getGridUserStrategy.execute(model);
  }
}
