package com.cs.core.config.strategy.usecase.organization;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IGetAllOrganizationResponseModel;

public interface IGetAllOrganizationsStrategy
    extends IConfigStrategy<IConfigGetAllRequestModel, IGetAllOrganizationResponseModel> {
  
}
