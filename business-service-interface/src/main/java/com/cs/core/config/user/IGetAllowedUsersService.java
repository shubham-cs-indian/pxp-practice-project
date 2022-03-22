package com.cs.core.config.user;

import com.cs.config.businessapi.base.IConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.user.IGetAllowedUsersRequestModel;
import com.cs.core.config.interactor.model.user.IUserModel;

public interface IGetAllowedUsersService
    extends IConfigService<IGetAllowedUsersRequestModel, IListModel<IUserModel>> {
  
}
