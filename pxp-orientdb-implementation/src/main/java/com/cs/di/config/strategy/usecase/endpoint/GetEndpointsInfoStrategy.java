package com.cs.di.config.strategy.usecase.endpoint;

import com.cs.core.config.interactor.model.endpoint.GetEndpointsInfoModel;
import com.cs.core.config.interactor.model.endpoint.IGetEndointsInfoModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.endpoint.IGetEndpointsInfoStrategy;
import com.cs.core.runtime.interactor.model.dataintegration.IDataIntegrationRequestModel;
import org.springframework.stereotype.Component;


@Component("getEndpointsInfoStrategy")
public class GetEndpointsInfoStrategy extends OrientDBBaseStrategy
    implements IGetEndpointsInfoStrategy {
  
  public static final String useCase = "GetEndpointInfoForDashboard";

  @Override public IGetEndointsInfoModel execute(IDataIntegrationRequestModel model)
      throws Exception
  {
    return execute(useCase, model, GetEndpointsInfoModel.class);
  }
}
