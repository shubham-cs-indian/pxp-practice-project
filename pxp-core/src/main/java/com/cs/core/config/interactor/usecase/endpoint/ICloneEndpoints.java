package com.cs.core.config.interactor.usecase.endpoint;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.IBulkSaveEndpointsResponseModel;
import com.cs.core.config.interactor.model.endpoint.ICloneEndpointModel;

public interface ICloneEndpoints extends
    ICreateConfigInteractor<IListModel<ICloneEndpointModel>, IBulkSaveEndpointsResponseModel> {
  
}
