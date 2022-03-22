package com.cs.di.config.strategy.usecase.endpoint;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.endpoint.GetEndpointForGridModel;
import com.cs.core.config.interactor.model.endpoint.IGetEndpointForGridModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.endpoint.IGetEndpointStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Component("getEndpointStrategy")
public class OrientDBGetEndpointStrategy extends OrientDBBaseStrategy
    implements IGetEndpointStrategy {
  
  public static final String useCase = "GetEndpoint";

  @Override public IGetEndpointForGridModel execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("id", model.getId());
    return execute(useCase, requestMap, GetEndpointForGridModel.class);
  }

}
