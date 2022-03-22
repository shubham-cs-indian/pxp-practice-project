package com.cs.core.config.role;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.role.ISystemsVsEndpointsModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetSelectedSystemInOrganizationService
    extends IGetConfigService<IIdParameterModel, IListModel<ISystemsVsEndpointsModel>> {
  
}
