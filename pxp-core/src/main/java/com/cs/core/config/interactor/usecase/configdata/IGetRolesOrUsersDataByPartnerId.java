package com.cs.core.config.interactor.usecase.configdata;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataResponseModel;
import com.cs.core.config.interactor.model.user.IGetRolesOrUsersDataByPartnerIdModel;

public interface IGetRolesOrUsersDataByPartnerId
    extends IGetConfigInteractor<IGetRolesOrUsersDataByPartnerIdModel, IGetConfigDataResponseModel> {
  
}
