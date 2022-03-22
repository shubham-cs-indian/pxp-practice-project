package com.cs.di.config.businessapi.endpoint;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.IBulkSaveEndpointsResponseModel;
import com.cs.core.config.interactor.model.endpoint.ICloneEndpointModel;

public interface ICloneEndpointsService extends ICreateConfigService<IListModel<ICloneEndpointModel>, IBulkSaveEndpointsResponseModel> {
  
}
