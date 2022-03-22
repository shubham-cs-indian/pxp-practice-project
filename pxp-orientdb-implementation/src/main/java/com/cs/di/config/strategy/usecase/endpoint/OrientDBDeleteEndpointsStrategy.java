package com.cs.di.config.strategy.usecase.endpoint;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.config.interactor.model.configdetails.BulkDeleteReturnModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.endpoint.IDeleteEndpointStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("deleteEndpointStrategy")
public class OrientDBDeleteEndpointsStrategy extends OrientDBBaseStrategy
    implements IDeleteEndpointStrategy {
  
  public static final String useCase = "DeleteEndpoint";

  @Override public IBulkDeleteReturnModel execute(IIdsListParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("ids", model.getIds());
    requestMap.put(IEndpoint.ENDPOINT_TYPE, CommonConstants.ONBOARDING_ENDPOINT);
    return execute(useCase, requestMap, BulkDeleteReturnModel.class);
  }

}
