package com.cs.di.config.strategy.usecase.endpoint;

import com.cs.core.config.interactor.model.endpoint.GetEndpointsInfoModel;
import com.cs.core.config.interactor.model.endpoint.IGetEndointsInfoModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.endpoint.IGetEndpointForDashboardBySystemIdStrategy;
import com.cs.core.runtime.interactor.model.dataintegration.IDataIntegrationRequestModel;
import org.springframework.stereotype.Component;

@Component("getEndpointForDashboardBySystemIdStrategy")
public class GetEndpointsForDashboardBySystemIdStrategy extends OrientDBBaseStrategy
    implements IGetEndpointForDashboardBySystemIdStrategy {

  @Override public IGetEndointsInfoModel execute(IDataIntegrationRequestModel model)
      throws Exception
  {
    return execute(GET_ENDPOINTS_FOR_DASHBOARD_BY_SYSTEM_ID, model, GetEndpointsInfoModel.class);
  }
}
