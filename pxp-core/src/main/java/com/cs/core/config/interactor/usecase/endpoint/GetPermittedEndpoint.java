package com.cs.core.config.interactor.usecase.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataResponseModel;
import com.cs.core.config.permission.IGetPermittedEndpointService;

@Service
public class GetPermittedEndpoint extends AbstractGetConfigInteractor<IGetConfigDataRequestModel, IGetConfigDataResponseModel>
    implements IGetPermittedEndpoint {
  
  @Autowired
  protected IGetPermittedEndpointService getPermittedEndpointService;
  
  @Override
  public IGetConfigDataResponseModel executeInternal(IGetConfigDataRequestModel dataModel) throws Exception
  {
    return getPermittedEndpointService.execute(dataModel);
  }
}
