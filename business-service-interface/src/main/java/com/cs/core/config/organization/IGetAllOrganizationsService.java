package com.cs.core.config.organization;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IGetAllOrganizationResponseModel;

public interface IGetAllOrganizationsService
    extends IGetConfigService<IConfigGetAllRequestModel, IGetAllOrganizationResponseModel> {
  
}
