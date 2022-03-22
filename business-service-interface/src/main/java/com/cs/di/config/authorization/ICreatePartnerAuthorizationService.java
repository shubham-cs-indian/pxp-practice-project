package com.cs.di.config.authorization;

import com.cs.config.businessapi.base.IConfigService;
import com.cs.di.config.model.authorization.IGetPartnerAuthorizationModel;
import com.cs.di.config.model.authorization.IPartnerAuthorizationModel;

public interface ICreatePartnerAuthorizationService extends IConfigService<IPartnerAuthorizationModel, IGetPartnerAuthorizationModel> {
  
}
