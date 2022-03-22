package com.cs.di.config.strategy.usecase.processevent;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.di.config.interactor.model.initializeworflowevent.DeleteProcessesEventResponseModel;
import com.cs.di.config.interactor.model.initializeworflowevent.IDeleteProcessEventResponseModel;

@Component("deleteProcessEventStrategy") public class OrientDBDeleteProcessEventStrategy
    extends OrientDBBaseStrategy implements IDeleteProcessEventStrategy {

  @Override public IDeleteProcessEventResponseModel execute(IIdsListParameterModel model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("ids", model.getIds());
    return execute(DELETE_PROCESS_EVENT, requestMap, DeleteProcessesEventResponseModel.class);
  }

}
