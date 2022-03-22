package com.cs.core.config.user;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.user.IGetGridUsersResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetGridUsersService
    extends IConfigStrategy<IConfigGetAllRequestModel, IGetGridUsersResponseModel> {
  
}
