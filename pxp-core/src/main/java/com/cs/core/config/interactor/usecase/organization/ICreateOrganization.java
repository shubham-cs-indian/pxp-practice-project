package com.cs.core.config.interactor.usecase.organization;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.organization.IGetOrganizationModel;
import com.cs.core.config.interactor.model.organization.IOrganizationModel;

public interface ICreateOrganization
    extends ICreateConfigInteractor<IOrganizationModel, IGetOrganizationModel> {
  
}
