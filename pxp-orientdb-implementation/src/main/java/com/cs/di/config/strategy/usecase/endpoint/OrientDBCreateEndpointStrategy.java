package com.cs.di.config.strategy.usecase.endpoint;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.endpoint.GetEndpointForGridModel;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.config.interactor.model.endpoint.IGetEndpointForGridModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.endpoint.ICreateEndpointStrategy;
@Component("createEndpointStrategy")
public class OrientDBCreateEndpointStrategy extends OrientDBBaseStrategy
    implements ICreateEndpointStrategy {
  
  public static final String useCase = "CreateEndpoint";

  @Override public IGetEndpointForGridModel execute(IEndpointModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("endpoint", model);
    return execute(useCase, requestMap, GetEndpointForGridModel.class);
  }

}
