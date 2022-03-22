package com.cs.core.config.organization;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.organization.IGetOrganizationModel;
import com.cs.core.config.interactor.model.organization.ISaveOrganizationModel;

public interface ISaveOrganizationService
    extends ISaveConfigService<ISaveOrganizationModel, IGetOrganizationModel> {
  
}
