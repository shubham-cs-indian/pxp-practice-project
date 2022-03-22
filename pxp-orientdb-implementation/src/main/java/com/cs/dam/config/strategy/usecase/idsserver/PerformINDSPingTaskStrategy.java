package com.cs.dam.config.strategy.usecase.idsserver;

import org.springframework.stereotype.Component;

import com.cs.core.runtime.interactor.constants.INDSConstants;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.dam.config.interactor.entity.idsserver.IInDesignServerInstance;
import com.cs.dam.config.interactor.model.idsserver.IINDSPingTaskRequestModel;
import com.cs.dam.config.interactor.model.idsserver.IINDSPingTaskResponseModel;
import com.cs.dam.config.interactor.model.idsserver.INDSPingTaskResponseModel;

/**
 * Get all InDesign instances for available load balancer details
 * @author mrunali.dhenge
 *
 */

@Component("performINDSPingTaskStrategy")
public class PerformINDSPingTaskStrategy extends BaseINDSStrategy
    implements IPerformINDSPingTaskStrategy {
  
  @Override
  public IINDSPingTaskResponseModel execute(IINDSPingTaskRequestModel requestModel) throws Exception
  {
    String requestData = ObjectMapperUtil.writeValueAsString(requestModel);
    IInDesignServerInstance loadBalancerProperties = requestModel.getIndsLoadBalancer();
    String response = sendRequest(requestData, INDSConstants.INDS_PING_REQUEST,
        loadBalancerProperties);
    return ObjectMapperUtil.readValue(response, INDSPingTaskResponseModel.class);
  }
  
}
