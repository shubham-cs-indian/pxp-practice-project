package com.cs.di.config.interactor.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.endpoint.IGetEnpointBySystemRequestModel;
import com.cs.core.config.interactor.model.endpoint.IGetGridEndpointsResponseModel;
import com.cs.di.config.businessapi.endpoint.IGetEndpointsBySystemService;

@Service
public class GetEndpointsBySystem extends
    AbstractGetConfigInteractor<IGetEnpointBySystemRequestModel, IGetGridEndpointsResponseModel>
    implements IGetGridEndpointsBySystem {
  
  @Autowired
  protected IGetEndpointsBySystemService getEndpointsService;
  
  @Override
  public IGetGridEndpointsResponseModel executeInternal(IGetEnpointBySystemRequestModel dataModel)
      throws Exception
  {
    return getEndpointsService.execute(dataModel);
  }

}
