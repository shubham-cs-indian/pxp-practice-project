package com.cs.di.config.strategy.authorization;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.di.base.IDiStrategy;
import com.cs.di.config.model.authorization.IGetAllPartnerAuthorizationModel;

public interface IGetAllPartnerAuthorizationStrategy
    extends IDiStrategy<IConfigGetAllRequestModel, IGetAllPartnerAuthorizationModel> {
  
}
