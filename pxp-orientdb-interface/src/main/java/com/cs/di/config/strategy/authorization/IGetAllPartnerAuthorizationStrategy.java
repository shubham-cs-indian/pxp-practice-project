package com.cs.di.config.strategy.authorization;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.di.config.model.authorization.IGetAllPartnerAuthorizationModel;

public interface IGetAllPartnerAuthorizationStrategy
    extends IConfigStrategy<IConfigGetAllRequestModel, IGetAllPartnerAuthorizationModel> {
  
}
