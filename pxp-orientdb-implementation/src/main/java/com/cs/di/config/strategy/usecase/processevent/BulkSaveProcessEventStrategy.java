package com.cs.di.config.strategy.usecase.processevent;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.di.config.interactor.model.initializeworflowevent.BulkProcessEventSaveResponseModel;
import com.cs.di.config.interactor.model.initializeworflowevent.IBulkProcessEventSaveResponseModel;
import com.cs.di.config.interactor.model.initializeworflowevent.ISaveProcessEventModel;

@Component("bulkSaveProcessEventStrategy")
	public class BulkSaveProcessEventStrategy
  extends OrientDBBaseStrategy implements IBulkSaveProcessEventStrategy {

  @Override
  public IBulkProcessEventSaveResponseModel execute(IListModel<ISaveProcessEventModel> model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put(IListModel.LIST, model);
    return execute(BULK_SAVE_PROCESS, requestMap, BulkProcessEventSaveResponseModel.class);
  }

}
