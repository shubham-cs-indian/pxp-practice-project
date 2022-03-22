package com.cs.di.config.businessapi.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.config.businessapi.base.Validations;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.config.interactor.model.endpoint.IGetEndpointForGridModel;
import com.cs.core.config.strategy.usecase.endpoint.ICreateEndpointStrategy;

@Service
public class CreateEndpointService extends AbstractCreateConfigService<IEndpointModel, IGetEndpointForGridModel>
    implements ICreateEndpointService {
  
  @Autowired
  ICreateEndpointStrategy createEndpointStrategy;
  
  @Override
  public IGetEndpointForGridModel executeInternal(IEndpointModel endpointModel) throws Exception
  {
    Validations.validateLabel(endpointModel.getLabel());
    return createEndpointStrategy.execute(endpointModel);
  }
  
}
