package com.cs.core.config.strategy.usecase.organization;

import com.cs.core.config.interactor.model.organization.IGetOrganizationModel;
import com.cs.core.config.interactor.model.organization.ISaveOrganizationModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ISaveOrganizationStrategy
    extends IConfigStrategy<ISaveOrganizationModel, IGetOrganizationModel> {
  
}
