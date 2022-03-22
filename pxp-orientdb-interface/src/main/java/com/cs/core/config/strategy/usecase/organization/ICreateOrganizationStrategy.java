package com.cs.core.config.strategy.usecase.organization;

import com.cs.core.config.interactor.model.organization.IGetOrganizationModel;
import com.cs.core.config.interactor.model.organization.IOrganizationModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ICreateOrganizationStrategy
    extends IConfigStrategy<IOrganizationModel, IGetOrganizationModel> {
  
}
