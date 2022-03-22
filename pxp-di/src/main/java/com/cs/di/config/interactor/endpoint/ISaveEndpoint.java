package com.cs.di.config.interactor.endpoint;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.IBulkSaveEndpointsResponseModel;
import com.cs.core.config.interactor.model.endpoint.ISaveEndpointModel;

public interface ISaveEndpoint
    extends ISaveConfigInteractor<IListModel<ISaveEndpointModel>, IBulkSaveEndpointsResponseModel> {
  
}
