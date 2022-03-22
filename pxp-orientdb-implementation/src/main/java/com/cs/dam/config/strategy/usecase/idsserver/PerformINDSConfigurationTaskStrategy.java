package com.cs.dam.config.strategy.usecase.idsserver;

import org.springframework.stereotype.Component;

import com.cs.core.runtime.interactor.constants.INDSConstants;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.dam.config.interactor.entity.idsserver.IInDesignServerInstance;
import com.cs.dam.config.interactor.model.idsserver.IINDSConfigurationTaskRequestModel;
import com.cs.dam.config.interactor.model.idsserver.IINDSPingTaskResponseModel;
import com.cs.dam.config.interactor.model.idsserver.INDSPingTaskResponseModel;

@Component("performINDSConfigurationTaskStrategy")
public class PerformINDSConfigurationTaskStrategy extends BaseINDSStrategy
    implements IPerformINDSConfigurationTaskStrategy {
  
  @Override
  public IINDSPingTaskResponseModel execute(IINDSConfigurationTaskRequestModel requestModel)
      throws Exception
  {
    String requestData = ObjectMapperUtil.writeValueAsString(requestModel);
    IInDesignServerInstance loadBalancerProperties = requestModel.getIndsLoadBalancer();
    String response = sendRequest(requestData, INDSConstants.INDS_CONFIGURATION_REQUEST,
        loadBalancerProperties);
    return ObjectMapperUtil.readValue(response, INDSPingTaskResponseModel.class);
  }
}
