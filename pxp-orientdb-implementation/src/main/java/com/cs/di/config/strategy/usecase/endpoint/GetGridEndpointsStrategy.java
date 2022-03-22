package com.cs.di.config.strategy.usecase.endpoint;

import com.cs.core.config.interactor.model.endpoint.GetGridEndpointsResponseModel;
import com.cs.core.config.interactor.model.endpoint.IGetGridEndpointsResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.endpoint.IGetGridEndpointsStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetGridEndpointsStrategy extends OrientDBBaseStrategy
    implements IGetGridEndpointsStrategy {
  
  @Override
  public IGetGridEndpointsResponseModel execute(IConfigGetAllRequestModel model) throws Exception
  {
    return execute(GET_GRID_ENDPOINTS, model, GetGridEndpointsResponseModel.class);
  }

}
