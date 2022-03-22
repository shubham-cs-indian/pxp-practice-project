package com.cs.core.runtime.strategy.usecase.dataintegration;

import com.cs.core.runtime.interactor.model.dataintegration.IGetEndpointsAndOrganisationIdRequestModel;
import com.cs.core.runtime.interactor.model.dataintegration.IGetEndpointsAndOrganisationIdResponseModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetEndpointIdsOfCustomTypeProcessStrategy extends
    IRuntimeStrategy<IGetEndpointsAndOrganisationIdRequestModel, IGetEndpointsAndOrganisationIdResponseModel> {
  
}
