package com.cs.di.config.interactor.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.endpoint.IGetEndpointForGridModel;
import com.cs.core.config.interactor.usecase.endpoint.IGetEndpoint;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.di.config.businessapi.endpoint.IGetEndpointService;

@Service
public class GetEndpoint
    extends AbstractGetConfigInteractor<IIdParameterModel, IGetEndpointForGridModel>
    implements IGetEndpoint {

  @Autowired
  protected IGetEndpointService getEndpointService;

  @Override public IGetEndpointForGridModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getEndpointService.execute(dataModel);
  }

}
