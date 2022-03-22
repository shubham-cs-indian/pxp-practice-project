package com.cs.core.config.interactor.usecase.organization;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.organization.IGetOrganizationModel;
import com.cs.core.config.interactor.model.organization.ISaveOrganizationModel;

public interface ISaveOrganization
    extends ISaveConfigInteractor<ISaveOrganizationModel, IGetOrganizationModel> {
  
}
