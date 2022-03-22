package com.cs.core.config.user;

import com.cs.config.businessapi.base.IConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.user.IUserInformationModel;
import com.cs.core.config.interactor.model.user.IUserModel;

public interface IGetAllUsersService
    extends IConfigService<IUserModel, IListModel<IUserInformationModel>> {
  
}
