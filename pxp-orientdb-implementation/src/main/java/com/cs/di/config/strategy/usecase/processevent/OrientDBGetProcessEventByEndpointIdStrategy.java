package com.cs.di.config.strategy.usecase.processevent;

import com.cs.core.config.interactor.model.processevent.GetProcessEndpointEventModel;
import com.cs.core.config.interactor.model.processevent.IGetProcessEndpointEventModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.strategy.usecase.dataintegration.IGetProcessEventByEndpointIdStrategy;
import org.springframework.stereotype.Component;

@Component("getProcessEventByEndpointIdStrategy")
public class OrientDBGetProcessEventByEndpointIdStrategy extends OrientDBBaseStrategy
    implements IGetProcessEventByEndpointIdStrategy {

  @Override public IGetProcessEndpointEventModel execute(IIdParameterModel model) throws Exception
  {
    return execute(GET_PROCESS_EVENT_BY_ENDPOINT_ID, model, GetProcessEndpointEventModel.class);
  }

}
