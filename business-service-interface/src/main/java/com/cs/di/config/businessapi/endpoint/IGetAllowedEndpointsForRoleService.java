package com.cs.di.config.businessapi.endpoint;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetAllowedEndpointsForRoleService extends IGetConfigService<IIdParameterModel, IListModel<IEndpointModel>> {
  
}
