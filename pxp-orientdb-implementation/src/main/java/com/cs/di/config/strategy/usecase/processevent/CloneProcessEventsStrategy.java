package com.cs.di.config.strategy.usecase.processevent;


import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.di.config.interactor.model.initializeworflowevent.IBulkProcessEventSaveResponseModel;
import com.cs.di.config.interactor.model.initializeworflowevent.ICloneProcessEventModel;
import com.cs.di.config.interactor.model.initializeworflowevent.BulkProcessEventSaveResponseModel;



@Component("cloneProcessEventsStrategy")
public class CloneProcessEventsStrategy extends OrientDBBaseStrategy implements ICloneProcessEventsStrategy {
  
  @Override
  public IBulkProcessEventSaveResponseModel execute(IListModel<ICloneProcessEventModel> model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put(IListModel.LIST, model);
    return execute(CLONE_PROCESS_EVENTS, requestMap, BulkProcessEventSaveResponseModel.class);
  }
  
}
