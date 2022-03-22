package com.cs.di.config.strategy.usecase.processevent;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.di.config.interactor.model.initializeworflowevent.GetAllProcessEventsResponseModel;
import com.cs.di.config.interactor.model.initializeworflowevent.IGetAllProcessEventsResponseModel;

@Component("getAllProcessEventsStrategy") public class OrientDBGetAllProcessEventsStrategy
    extends OrientDBBaseStrategy implements IGetAllProcessEventsStrategy {

  @Override public IGetAllProcessEventsResponseModel execute(IConfigGetAllRequestModel model)
      throws Exception
  {
    return execute(GET_PROCESS_EVENTS, model, GetAllProcessEventsResponseModel.class);
  }

}
