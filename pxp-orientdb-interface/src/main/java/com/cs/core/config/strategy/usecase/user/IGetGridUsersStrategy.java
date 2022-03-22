package com.cs.core.config.strategy.usecase.user;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.user.IGetGridUsersResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetGridUsersStrategy
    extends IConfigStrategy<IConfigGetAllRequestModel, IGetGridUsersResponseModel> {
  
}
