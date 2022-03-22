package com.cs.di.config.strategy.usecase.processevent;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.processevent.CreateOrSaveProcessEventResponseModel;
import com.cs.core.config.interactor.model.processevent.IProcessEventModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component("createProcessEventStrategy") public class OrientDBCreateProcessEventStrategy
    extends OrientDBBaseStrategy implements ICreateProcessEventStrategy {

  @Override public CreateOrSaveProcessEventResponseModel execute(IProcessEventModel model) throws Exception
  {
    return execute(CREATE_PROCESS_EVENT, model, CreateOrSaveProcessEventResponseModel.class);
  }

}
