package com.cs.core.config.interactor.usecase.idsserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.idsserver.IUpdateINDSInstancesDataService;
import com.cs.dam.config.interactor.model.idsserver.IINDSConfigurationTaskRequestModel;
import com.cs.dam.config.interactor.model.idsserver.IINDSPingTaskResponseModel;

/**
 * Add, delete and update indesign server and save load balancer information
 * @author mrunali.dhenge
 *
 */
@Service
public class UpdateINDSInstancesData
    extends AbstractSaveConfigInteractor<IINDSConfigurationTaskRequestModel, IINDSPingTaskResponseModel>
    implements IUpdateINDSInstancesData {
  
  @Autowired
  protected IUpdateINDSInstancesDataService updateINDSInstancesDataService;
  
  public IINDSPingTaskResponseModel executeInternal(IINDSConfigurationTaskRequestModel requestModel)
      throws Exception
  {
    return updateINDSInstancesDataService.execute(requestModel);
  }
}
