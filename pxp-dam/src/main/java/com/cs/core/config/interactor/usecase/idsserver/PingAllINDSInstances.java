package com.cs.core.config.interactor.usecase.idsserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.idsserver.IPingAllINDSInstancesService;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.dam.config.interactor.model.idsserver.IINDSPingTaskResponseModel;

/**
 * Interactor to get all InDesign instances with load balancer details
 * 
 * @author mrunali.dhenge
 *
 */
@Service
public class PingAllINDSInstances extends AbstractGetConfigService<IModel, IINDSPingTaskResponseModel> implements IPingAllINDSInstances {
  
  @Autowired
  protected IPingAllINDSInstancesService pingAllINDSInstancesService;
  
  @Override
  public IINDSPingTaskResponseModel executeInternal(IModel requestModel) throws Exception
  {
    return pingAllINDSInstancesService.execute(requestModel);
  }
}
