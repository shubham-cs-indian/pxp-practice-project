package com.cs.di.config.businessapi.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.config.strategy.usecase.endpoint.IGetAllEndpointsStrategy;

@Service
public class GetAllEndpointsService extends AbstractGetConfigService<IEndpointModel, IListModel<IEndpoint>>
    implements IGetAllEndpointsService {
  
  @Autowired
  protected IGetAllEndpointsStrategy getAllEndpointsStrategy;
  
  @Override
  public IListModel<IEndpoint> executeInternal(IEndpointModel dataModel) throws Exception
  {
    return getAllEndpointsStrategy.execute(dataModel);
  }
  
}
