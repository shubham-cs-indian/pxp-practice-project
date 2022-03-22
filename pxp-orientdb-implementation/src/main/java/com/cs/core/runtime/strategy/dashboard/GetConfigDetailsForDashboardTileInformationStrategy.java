package com.cs.core.runtime.strategy.dashboard;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.ConfigDetailsForInstanceTreeGetModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForInstanceTreeGetModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

@Component
public class GetConfigDetailsForDashboardTileInformationStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsForDashboardTileInformationStrategy {
  
  @Override
  public IConfigDetailsForInstanceTreeGetModel execute(IIdParameterModel model) throws Exception
  {
    return execute(GET_CONFIG_DETAILS_FOR_DASHBOARD_TILE_INFORMATION, model,
        ConfigDetailsForInstanceTreeGetModel.class);
  }
}
