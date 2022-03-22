package com.cs.di.config.strategy.authorization;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.di.config.model.authorization.IGetPartnerAuthorizationModel;
import com.cs.di.config.model.authorization.ISavePartnerAuthorizationModel;

public interface ISavePartnerAuthorizationStrategy extends IConfigStrategy<ISavePartnerAuthorizationModel, IGetPartnerAuthorizationModel> {
  
}
