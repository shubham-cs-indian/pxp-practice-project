package com.cs.di.config.strategy.authorization;

import com.cs.di.base.IDiStrategy;
import com.cs.di.config.model.authorization.IGetPartnerAuthorizationModel;
import com.cs.di.config.model.authorization.ISavePartnerAuthorizationModel;

public interface ISavePartnerAuthorizationStrategy extends IDiStrategy<ISavePartnerAuthorizationModel, IGetPartnerAuthorizationModel> {
  
}
