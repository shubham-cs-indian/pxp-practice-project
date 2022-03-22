package com.cs.core.config.strategy.usecase.configdata;

import com.cs.core.config.interactor.model.configdetails.IGetConfigDataResponseModel;
import com.cs.core.config.interactor.model.user.IGetRolesOrUsersDataByPartnerIdModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetRolesOrUsersDataByPartnerIdStrategy
    extends IConfigStrategy<IGetRolesOrUsersDataByPartnerIdModel, IGetConfigDataResponseModel> {
  
}
