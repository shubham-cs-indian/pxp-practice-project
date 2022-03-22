package com.cs.core.config.interactor.usecase.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.user.IBulkSaveUsersResponseModel;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.user.ISaveUsersService;

@Service
public class SaveUsers
    extends AbstractSaveConfigInteractor<IListModel<IUserModel>, IBulkSaveUsersResponseModel>
    implements ISaveUsers {
  
  @Autowired
  ISaveUsersService saveUserService;
  
  @Override
  public IBulkSaveUsersResponseModel executeInternal(IListModel<IUserModel> userModels) throws Exception
  {
    return saveUserService.execute(userModels);
  }
}
