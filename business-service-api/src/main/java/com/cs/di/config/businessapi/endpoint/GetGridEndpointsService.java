package com.cs.di.config.businessapi.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.endpoint.IGetGridEndpointsResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.strategy.usecase.endpoint.IGetGridEndpointsStrategy;

@Service
public class GetGridEndpointsService extends AbstractGetConfigService<IConfigGetAllRequestModel, IGetGridEndpointsResponseModel>
    implements IGetGridEndpointsService {
  
  @Autowired
  protected IGetGridEndpointsStrategy getGridEndpointsStrategy;
  
  @Override
  public IGetGridEndpointsResponseModel executeInternal(IConfigGetAllRequestModel dataModel) throws Exception
  {
    return getGridEndpointsStrategy.execute(dataModel);
  }
  
}
