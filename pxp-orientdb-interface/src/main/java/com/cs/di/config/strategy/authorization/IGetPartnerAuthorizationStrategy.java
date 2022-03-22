package com.cs.di.config.strategy.authorization;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.di.config.model.authorization.IGetPartnerAuthorizationModel;

public interface IGetPartnerAuthorizationStrategy extends IConfigStrategy<IIdParameterModel, IGetPartnerAuthorizationModel> {
  
}
