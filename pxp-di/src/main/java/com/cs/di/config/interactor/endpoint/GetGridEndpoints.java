package com.cs.di.config.interactor.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.endpoint.IGetGridEndpointsResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.di.config.businessapi.endpoint.IGetGridEndpointsService;

@Service
public class GetGridEndpoints
    extends AbstractGetConfigInteractor<IConfigGetAllRequestModel, IGetGridEndpointsResponseModel>
    implements IGetGridEndpoints {
  
  @Autowired
  protected IGetGridEndpointsService getGridEndpointsService;
  
  @Override
  public IGetGridEndpointsResponseModel executeInternal(IConfigGetAllRequestModel dataModel)
      throws Exception
  {
    return getGridEndpointsService.execute(dataModel);
  }

}
