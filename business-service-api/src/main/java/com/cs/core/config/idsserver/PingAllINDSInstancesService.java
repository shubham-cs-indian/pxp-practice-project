package com.cs.core.config.idsserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.dam.config.interactor.model.idsserver.IINDSPingTaskRequestModel;
import com.cs.dam.config.interactor.model.idsserver.IINDSPingTaskResponseModel;
import com.cs.dam.config.interactor.model.idsserver.INDSPingTaskResponseModel;
import com.cs.dam.config.strategy.usecase.dtp.idsserver.IGetAllInDesignServerInstancesStrategy;
import com.cs.dam.config.strategy.usecase.idsserver.IPerformINDSPingTaskStrategy;

/**
 * Interactor to get all InDesign instances with load balancer details
 * 
 * @author mrunali.dhenge
 *
 */
@Service
public class PingAllINDSInstancesService extends AbstractIDSNServerTask<IModel, IINDSPingTaskResponseModel>
    implements IPingAllINDSInstancesService {
  
  @Autowired
  protected IGetAllInDesignServerInstancesStrategy getAllInDesignServerInstancesStrategy;
  
  @Autowired
  protected IPerformINDSPingTaskStrategy           performINDSPingTaskStrategy;
  
  @Override
  public IINDSPingTaskResponseModel executeInternal(IModel requestModel) throws Exception
  {
    super.executeInternal(requestModel);
    IINDSPingTaskRequestModel pingTaskRequestModel = getAllInDesignServerInstancesStrategy.execute(requestModel);
    if (pingTaskRequestModel.getIndsLoadBalancer() == null) {
      return new INDSPingTaskResponseModel();
    }
    IINDSPingTaskResponseModel responseModel = performINDSPingTaskStrategy.execute(pingTaskRequestModel);
    responseModel.setIndsLoadBalancer(pingTaskRequestModel.getIndsLoadBalancer());
    return responseModel;
  }
}
