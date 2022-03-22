package com.cs.di.config.businessapi.endpoint;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.IBulkSaveEndpointsResponseModel;
import com.cs.core.config.interactor.model.endpoint.ISaveEndpointModel;

public interface ISaveEndpointService extends ISaveConfigService<IListModel<ISaveEndpointModel>, IBulkSaveEndpointsResponseModel> {
  
}
