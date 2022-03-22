package com.cs.di.config.interactor.endpoint;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.IGetAllEndpointsByTypeRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;

public interface IGetAllEndpointsByType
    extends IGetConfigInteractor<IGetAllEndpointsByTypeRequestModel, IListModel<IIdLabelCodeModel>> {
  
}
