package com.cs.core.config.interactor.usecase.organization;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IGetAllOrganizationResponseModel;

public interface IGetAllOrganizations
    extends IGetConfigInteractor<IConfigGetAllRequestModel, IGetAllOrganizationResponseModel> {
  
}
