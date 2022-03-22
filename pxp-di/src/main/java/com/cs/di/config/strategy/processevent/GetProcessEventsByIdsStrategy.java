package com.cs.di.config.strategy.processevent;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.di.config.interactor.model.initializeworflowevent.GetProcessEventsByIdsResponseModel;
import com.cs.di.config.interactor.model.initializeworflowevent.IGetProcessEventsByIdsResponseModel;

@Component("getProcessEventsByIdsStrategy")
public class GetProcessEventsByIdsStrategy extends OrientDBBaseStrategy
    implements IGetProcessEventsByIdsStrategy {
  
  @Override
  public IGetProcessEventsByIdsResponseModel execute(IIdsListParameterModel model) throws Exception
  {
    return execute(GET_PROCESS_EVENTS_BY_IDS, model, GetProcessEventsByIdsResponseModel.class);
  }
  
}
