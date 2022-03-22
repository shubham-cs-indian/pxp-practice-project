package com.cs.core.config.interactor.usecase.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.user.IGetAllowedUsersRequestModel;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.user.IGetAllowedUsersService;

@Service
public class GetAllowedUsers
    extends AbstractGetConfigInteractor<IGetAllowedUsersRequestModel, IListModel<IUserModel>>
    implements IGetAllowedUsers {
  
  @Autowired
  IGetAllowedUsersService getAllowedUsersService;
  
  @Override
  public IListModel<IUserModel> executeInternal(IGetAllowedUsersRequestModel dataModel) throws Exception
  {
    return getAllowedUsersService.execute(dataModel);
  }
}
