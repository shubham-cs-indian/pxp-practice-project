package com.cs.di.config.strategy.usecase.processevent;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.processevent.GetProcessEventModel;
import com.cs.core.config.interactor.model.processevent.IGetProcessEventModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Component("getProcessEventStrategy") public class OrientDBGetProcessEventStrategy
    extends OrientDBBaseStrategy implements IGetProcessEventStrategy {

  @Override public IGetProcessEventModel execute(IIdParameterModel model) throws Exception
  {
    return execute(GET_PROCESS_EVENT, model, GetProcessEventModel.class);
  }

}
