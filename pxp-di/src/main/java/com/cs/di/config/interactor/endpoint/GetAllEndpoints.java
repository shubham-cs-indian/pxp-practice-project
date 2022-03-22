package com.cs.di.config.interactor.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.di.config.businessapi.endpoint.IGetAllEndpointsService;

@Service
public class GetAllEndpoints extends
    AbstractGetConfigInteractor<IEndpointModel, IListModel<IEndpoint>> implements IGetAllEndpoints {

  @Autowired
  protected IGetAllEndpointsService getAllEndpointsService;

  @Override public IListModel<IEndpoint> executeInternal(IEndpointModel dataModel) throws Exception
  {
    return getAllEndpointsService.execute(dataModel);
  }

}
