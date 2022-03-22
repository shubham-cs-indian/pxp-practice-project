package com.cs.core.config.idsserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.dam.config.interactor.model.idsserver.IINDSPingTaskRequestModel;
import com.cs.dam.config.interactor.model.idsserver.IINDSPingTaskResponseModel;
import com.cs.dam.config.strategy.usecase.idsserver.IPerformINDSPingTaskStrategy;

/**
 * Get status details of indesign server for particular port
 * 
 * @author mrunali.dhenge
 *
 */

@Service
public class PingINDSInstancesService extends AbstractIDSNServerTask<IINDSPingTaskRequestModel, IINDSPingTaskResponseModel>
    implements IPingINDSInstancesService {
  
  @Autowired
  protected IPerformINDSPingTaskStrategy performINDSPingTaskStrategy;
  
  public IINDSPingTaskResponseModel executeInternal(IINDSPingTaskRequestModel pingTaskRequestModel) throws Exception
  {
    // super.executeInternal(pingTaskRequestModel);
    
    return performINDSPingTaskStrategy.execute(pingTaskRequestModel);
  }
}
