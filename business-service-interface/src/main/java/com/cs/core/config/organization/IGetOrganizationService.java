package com.cs.core.config.organization;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.organization.IGetOrganizationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetOrganizationService
    extends IGetConfigService<IIdParameterModel, IGetOrganizationModel> {
  
}
