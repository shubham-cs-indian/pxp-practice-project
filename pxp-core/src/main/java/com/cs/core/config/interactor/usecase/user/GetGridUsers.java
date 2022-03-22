package com.cs.core.config.interactor.usecase.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.user.IGetGridUsersResponseModel;
import com.cs.core.config.user.IGetGridUsersService;

@Service
public class GetGridUsers
    extends AbstractGetConfigInteractor<IConfigGetAllRequestModel, IGetGridUsersResponseModel>
    implements IGetGridUsers {
  
  @Autowired
  protected IGetGridUsersService getGridUserService;
  
  @Override
  public IGetGridUsersResponseModel executeInternal(IConfigGetAllRequestModel model) throws Exception
  {
    return getGridUserService.execute(model);
  }
}
