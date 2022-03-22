package com.cs.di.config.businessapi.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.endpoint.IGetEnpointBySystemRequestModel;
import com.cs.core.config.interactor.model.endpoint.IGetGridEndpointsResponseModel;
import com.cs.core.config.strategy.usecase.endpoint.IGetEndpointsBySystemStrategy;

@Service
public class GetEndpointsBySystemService extends AbstractGetConfigService<IGetEnpointBySystemRequestModel, IGetGridEndpointsResponseModel>
    implements IGetEndpointsBySystemService {
  
  @Autowired
  protected IGetEndpointsBySystemStrategy getEndpointsBySystemStrategy;
  
  @Override
  public IGetGridEndpointsResponseModel executeInternal(IGetEnpointBySystemRequestModel dataModel) throws Exception
  {
    return getEndpointsBySystemStrategy.execute(dataModel);
  }
  
}
