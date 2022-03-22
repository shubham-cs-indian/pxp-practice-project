package com.cs.di.config.strategy.usecase.processevent;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.di.config.interactor.model.initializeworflowevent.GetGridProcessEventsResponseModel;
import com.cs.di.config.interactor.model.initializeworflowevent.IGetGridProcessEventsResponseModel;

@Component("getGridProcessEventsStrategy")
public class GetGridProcessEventsStrategy extends OrientDBBaseStrategy implements IGetGridProcessEventsStrategy {
  
  @Override
  public IGetGridProcessEventsResponseModel execute(IConfigGetAllRequestModel model) throws Exception
  {
    return execute(GET_GRID_PROCESS_EVENTS, model, GetGridProcessEventsResponseModel.class);
  }
}