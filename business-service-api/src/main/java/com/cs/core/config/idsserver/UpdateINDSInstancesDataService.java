package com.cs.core.config.idsserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.dam.config.interactor.model.idsserver.IINDSConfigurationTaskRequestModel;
import com.cs.dam.config.interactor.model.idsserver.IINDSConfigurationTaskResponseModel;
import com.cs.dam.config.interactor.model.idsserver.IINDSPingTaskResponseModel;
import com.cs.dam.config.strategy.usecase.dtp.idsserver.IUpdateInDesignServerInstanceDataStrategy;
import com.cs.dam.config.strategy.usecase.idsserver.IPerformINDSConfigurationTaskStrategy;

/**
 * Add, delete and update indesign server and save load balancer information
 * 
 * @author mrunali.dhenge
 *
 */
@Service
public class UpdateINDSInstancesDataService extends AbstractIDSNServerTask<IINDSConfigurationTaskRequestModel, IINDSPingTaskResponseModel>
    implements IUpdateINDSInstancesDataService {
  
  @Autowired
  protected IUpdateInDesignServerInstanceDataStrategy updateInDesignServerInstanceDataStrategy;
  
  @Autowired
  protected IPerformINDSConfigurationTaskStrategy     performINDSConfigurationTaskStrategy;
  
  public IINDSPingTaskResponseModel executeInternal(IINDSConfigurationTaskRequestModel requestModel) throws Exception
  {
    // super.executeInternal(requestModel);
    IINDSConfigurationTaskResponseModel configResponseModel = updateInDesignServerInstanceDataStrategy.execute(requestModel);
    requestModel.setServersToAdd(configResponseModel.getAddedServers());
    requestModel.setServersToUpdate(configResponseModel.getUpdatedServers());
    requestModel.setServersToRemove(configResponseModel.getRemovedServers());
    requestModel.setAllServers(configResponseModel.getAllServers());
    
    return performINDSConfigurationTaskStrategy.execute(requestModel);
  }
}
