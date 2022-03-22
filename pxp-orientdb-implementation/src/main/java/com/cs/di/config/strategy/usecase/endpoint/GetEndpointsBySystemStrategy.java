package com.cs.di.config.strategy.usecase.endpoint;

import com.cs.core.config.interactor.model.endpoint.GetGridEndpointsResponseModel;
import com.cs.core.config.interactor.model.endpoint.IGetEnpointBySystemRequestModel;
import com.cs.core.config.interactor.model.endpoint.IGetGridEndpointsResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.endpoint.IGetEndpointsBySystemStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetEndpointsBySystemStrategy extends OrientDBBaseStrategy
    implements IGetEndpointsBySystemStrategy {
  
  @Override
  public IGetGridEndpointsResponseModel execute(IGetEnpointBySystemRequestModel model)
      throws Exception
  {
    return execute(GET_ENDPOINTS_BY_SYSTEM, model, GetGridEndpointsResponseModel.class);
  }

}
