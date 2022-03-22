package com.cs.di.config.interactor.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.config.interactor.model.endpoint.IGetEndpointForGridModel;
import com.cs.di.config.businessapi.endpoint.ICreateEndpointService;

@Service
public class CreateEndpoint
    extends AbstractCreateConfigInteractor<IEndpointModel, IGetEndpointForGridModel>
    implements ICreateEndpoint {
  
  @Autowired
  protected ICreateEndpointService createEndpointService;
  
  @Override
  public IGetEndpointForGridModel executeInternal(IEndpointModel endpointModel) throws Exception
  {
    return createEndpointService.execute(endpointModel);
  }

}
