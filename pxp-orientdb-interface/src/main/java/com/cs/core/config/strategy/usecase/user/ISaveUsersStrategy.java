package com.cs.core.config.strategy.usecase.user;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.user.IBulkSaveUsersResponseModel;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ISaveUsersStrategy
    extends IConfigStrategy<IListModel<IUserModel>, IBulkSaveUsersResponseModel> {
  
}
