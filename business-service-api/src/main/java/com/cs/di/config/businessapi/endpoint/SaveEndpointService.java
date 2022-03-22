package com.cs.di.config.businessapi.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.IBulkSaveEndpointsResponseModel;
import com.cs.core.config.interactor.model.endpoint.ISaveEndpointModel;
import com.cs.core.config.strategy.usecase.endpoint.ISaveEndpointStrategy;

@Service
public class SaveEndpointService extends AbstractSaveConfigService<IListModel<ISaveEndpointModel>, IBulkSaveEndpointsResponseModel>
    implements ISaveEndpointService {
  
  @Autowired
  protected ISaveEndpointStrategy  saveEndpointStrategy;
  
  @Override
  public IBulkSaveEndpointsResponseModel executeInternal(IListModel<ISaveEndpointModel> saveEndpointsModel) throws Exception
  {
    return saveEndpointStrategy.execute(saveEndpointsModel);
  }
  
}
