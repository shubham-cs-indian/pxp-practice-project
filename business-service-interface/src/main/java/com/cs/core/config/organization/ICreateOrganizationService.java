package com.cs.core.config.organization;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.organization.IGetOrganizationModel;
import com.cs.core.config.interactor.model.organization.IOrganizationModel;

public interface ICreateOrganizationService
    extends ICreateConfigService<IOrganizationModel, IGetOrganizationModel> {
  
}
