package com.cs.di.config.interactor.endpoint;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetAllowedEndpointsForRole
    extends IGetConfigInteractor<IIdParameterModel, IListModel<IEndpointModel>> {
  
}
