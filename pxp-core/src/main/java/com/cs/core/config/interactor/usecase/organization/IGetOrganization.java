package com.cs.core.config.interactor.usecase.organization;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.organization.IGetOrganizationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetOrganization
    extends IGetConfigInteractor<IIdParameterModel, IGetOrganizationModel> {
  
}
