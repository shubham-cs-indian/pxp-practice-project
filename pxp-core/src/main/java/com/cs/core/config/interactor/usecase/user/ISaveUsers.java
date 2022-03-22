package com.cs.core.config.interactor.usecase.user;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.config.interactor.usecase.base.IConfigInteractor;
import com.cs.core.config.interactor.model.user.IBulkSaveUsersResponseModel;
import com.cs.core.config.interactor.model.user.IUserModel;

public interface ISaveUsers
    extends IConfigInteractor<IListModel<IUserModel>, IBulkSaveUsersResponseModel> {
  
}
