package com.cs.core.config.strategy.usecase.role;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.role.ISystemsVsEndpointsModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetSelectedSystemInOrganizationStrategy
    extends IConfigStrategy<IIdParameterModel, IListModel<ISystemsVsEndpointsModel>> {
  
}
