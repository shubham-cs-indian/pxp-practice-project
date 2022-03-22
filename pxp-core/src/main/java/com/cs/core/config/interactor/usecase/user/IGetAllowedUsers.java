package com.cs.core.config.interactor.usecase.user;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.config.interactor.usecase.base.IConfigInteractor;
import com.cs.core.config.interactor.model.user.IGetAllowedUsersRequestModel;
import com.cs.core.config.interactor.model.user.IUserModel;

public interface IGetAllowedUsers
    extends IConfigInteractor<IGetAllowedUsersRequestModel, IListModel<IUserModel>> {
  
}
