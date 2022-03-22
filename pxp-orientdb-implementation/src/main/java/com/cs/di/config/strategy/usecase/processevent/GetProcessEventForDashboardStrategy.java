package com.cs.di.config.strategy.usecase.processevent;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.GetConfigDataEntityResponseModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component("getProcessEventForDashboardStrategy")
public class GetProcessEventForDashboardStrategy extends OrientDBBaseStrategy
    implements IGetProcessEventForDashboardStrategy {

  @Override
  public IGetConfigDataEntityResponseModel execute(IConfigGetAllRequestModel model)
      throws Exception
  {
    return execute(GET_PROCESS_EVENTS_FOR_DASHBOARD, model, GetConfigDataEntityResponseModel.class);
  }
}
