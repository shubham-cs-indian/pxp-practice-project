package com.cs.core.config.interactor.usecase.idsserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.idsserver.IPingINDSInstancesService;
import com.cs.dam.config.interactor.model.idsserver.IINDSPingTaskRequestModel;
import com.cs.dam.config.interactor.model.idsserver.IINDSPingTaskResponseModel;

/**
 * Get status details of indesign server for particular port
 * @author mrunali.dhenge
 *
 */

@Service
public class PingINDSInstances extends AbstractGetConfigService<IINDSPingTaskRequestModel, IINDSPingTaskResponseModel>
    implements IPingINDSInstances {
  
  @Autowired
  protected IPingINDSInstancesService pingINDSInstancesService;
  
  public IINDSPingTaskResponseModel executeInternal(IINDSPingTaskRequestModel pingTaskRequestModel)
      throws Exception
  {
    return pingINDSInstancesService.execute(pingTaskRequestModel);
  }
}
