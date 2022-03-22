package com.cs.di.config.strategy.authorization;

import com.cs.di.base.IDiStrategy;
import com.cs.di.config.model.authorization.IPartnerAuthorizationModel;
import com.cs.di.config.model.authorization.IGetPartnerAuthorizationModel;

public interface ICreatePartnerAuthorizationStrategy extends  IDiStrategy<IPartnerAuthorizationModel, IGetPartnerAuthorizationModel> {
  
}
