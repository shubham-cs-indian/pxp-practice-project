package com.cs.di.config.strategy.usecase.endpoint;

import com.cs.core.config.interactor.model.endpoint.GetConfigDetailsForEndpointModel;
import com.cs.core.config.interactor.model.endpoint.IGetConfigDetailsForEndpointModel;
import com.cs.core.config.interactor.model.endpoint.IGetConfigDetailsForExportRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.endpoint.IGetConfigDetailsForEndpointStrategy;
import org.springframework.stereotype.Component;

@Component("getConfigDetailsForEndpoint") public class GetConfigDetailsForEndpointStrategy
    extends OrientDBBaseStrategy implements IGetConfigDetailsForEndpointStrategy {

  public static final String useCase = "GetConfigDetailsForEndpoint";

  @Override
  public IGetConfigDetailsForEndpointModel execute(IGetConfigDetailsForExportRequestModel model)
      throws Exception
  {
    return execute(useCase, model, GetConfigDetailsForEndpointModel.class);
  }

}
