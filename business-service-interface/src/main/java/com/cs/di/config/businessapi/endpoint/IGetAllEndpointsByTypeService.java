package com.cs.di.config.businessapi.endpoint;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.IGetAllEndpointsByTypeRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;

public interface IGetAllEndpointsByTypeService
    extends IGetConfigService<IGetAllEndpointsByTypeRequestModel, IListModel<IIdLabelCodeModel>> {
  
}
