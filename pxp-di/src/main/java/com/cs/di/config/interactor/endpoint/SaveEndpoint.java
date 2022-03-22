package com.cs.di.config.interactor.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.IBulkSaveEndpointsResponseModel;
import com.cs.core.config.interactor.model.endpoint.ISaveEndpointModel;
import com.cs.di.config.businessapi.endpoint.ISaveEndpointService;

@Service
public class SaveEndpoint extends
    AbstractSaveConfigInteractor<IListModel<ISaveEndpointModel>, IBulkSaveEndpointsResponseModel>
    implements ISaveEndpoint {

  @Autowired
  protected ISaveEndpointService saveEndpointService;

  @Override
  public IBulkSaveEndpointsResponseModel executeInternal(IListModel<ISaveEndpointModel> saveEndpointsModel)
      throws Exception
  {
    return saveEndpointService.execute(saveEndpointsModel);
  }

}
