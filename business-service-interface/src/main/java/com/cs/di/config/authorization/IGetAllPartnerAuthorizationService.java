package com.cs.di.config.authorization;

import com.cs.config.businessapi.base.IConfigService;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.di.config.model.authorization.IGetAllPartnerAuthorizationModel;

public interface IGetAllPartnerAuthorizationService extends IConfigService<IConfigGetAllRequestModel, IGetAllPartnerAuthorizationModel> {
  
}