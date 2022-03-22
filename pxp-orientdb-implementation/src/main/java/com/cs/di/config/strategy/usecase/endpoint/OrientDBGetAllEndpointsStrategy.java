package com.cs.di.config.strategy.usecase.endpoint;

import com.cs.core.config.interactor.entity.endpoint.Endpoint;
import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.endpoint.IGetAllEndpointsStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getAllEndpointsStrategy") public class OrientDBGetAllEndpointsStrategy
    extends OrientDBBaseStrategy implements IGetAllEndpointsStrategy {

  public static final String useCase = "GetAllEndpoints";

  @Override public IListModel<IEndpoint> execute(IEndpointModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put(IEndpoint.ENDPOINT_TYPE, model.getEndpointType());
    return execute(useCase, requestMap, new TypeReference<ListModel<Endpoint>>()
    {
    });
  }

}
