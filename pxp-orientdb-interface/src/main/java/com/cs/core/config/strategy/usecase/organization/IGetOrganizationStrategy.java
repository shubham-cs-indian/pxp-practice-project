package com.cs.core.config.strategy.usecase.organization;

import com.cs.core.config.interactor.model.organization.IGetOrganizationModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetOrganizationStrategy
    extends IConfigStrategy<IIdParameterModel, IGetOrganizationModel> {
  
}
