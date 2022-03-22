package com.cs.core.config.interactor.usecase.role;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.role.ISystemsVsEndpointsModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetSelectedSystemInOrganization
    extends IGetConfigInteractor<IIdParameterModel, IListModel<ISystemsVsEndpointsModel>> {
  
}
