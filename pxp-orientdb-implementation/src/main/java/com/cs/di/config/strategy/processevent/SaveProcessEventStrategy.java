package com.cs.di.config.strategy.processevent;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.processevent.CreateOrSaveProcessEventResponseModel;
import com.cs.core.config.interactor.model.processevent.ICreateOrSaveProcessEventResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.di.config.interactor.model.initializeworflowevent.ISaveProcessEventModel;

@Component("saveProcessEventStrategy")
public class SaveProcessEventStrategy extends OrientDBBaseStrategy implements ISaveProcessEventStrategy {
  
  @Override
  public ICreateOrSaveProcessEventResponseModel execute(ISaveProcessEventModel model) throws Exception
  {
    return execute(SAVE_PROCESS_EVENT, model, CreateOrSaveProcessEventResponseModel.class);
  }
  
}
