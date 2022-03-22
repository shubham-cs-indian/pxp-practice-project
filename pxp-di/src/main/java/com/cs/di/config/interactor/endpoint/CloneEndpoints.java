package com.cs.di.config.interactor.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.IBulkSaveEndpointsResponseModel;
import com.cs.core.config.interactor.model.endpoint.ICloneEndpointModel;
import com.cs.core.config.interactor.usecase.endpoint.ICloneEndpoints;
import com.cs.di.config.businessapi.endpoint.ICloneEndpointsService;

@Service
public class CloneEndpoints extends
    AbstractCreateConfigInteractor<IListModel<ICloneEndpointModel>, IBulkSaveEndpointsResponseModel>
    implements ICloneEndpoints {
  
  @Autowired
  protected ICloneEndpointsService            cloneEndpointService;
  
  @Override
  public IBulkSaveEndpointsResponseModel executeInternal(
      IListModel<ICloneEndpointModel> cloneEndpointsListModel) throws Exception
  {
    return cloneEndpointService.execute(cloneEndpointsListModel);
  }
}
