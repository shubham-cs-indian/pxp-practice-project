package com.cs.di.config.businessapi.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.endpoint.IGetEndpointForGridModel;
import com.cs.core.config.strategy.usecase.endpoint.IGetEndpointStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetEndpointService extends AbstractGetConfigService<IIdParameterModel, IGetEndpointForGridModel>
    implements IGetEndpointService {
  
  @Autowired
  protected IGetEndpointStrategy getEndpointStrategy;
  
  @Override
  public IGetEndpointForGridModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getEndpointStrategy.execute(dataModel);
  }
  
}
