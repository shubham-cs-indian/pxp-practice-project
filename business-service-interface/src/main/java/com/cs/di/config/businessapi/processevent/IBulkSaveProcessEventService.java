package com.cs.di.config.businessapi.processevent;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.di.config.interactor.model.initializeworflowevent.IBulkProcessEventSaveResponseModel;
import com.cs.di.config.interactor.model.initializeworflowevent.ISaveProcessEventModel;

public interface IBulkSaveProcessEventService
    extends ISaveConfigService<IListModel<ISaveProcessEventModel>, IBulkProcessEventSaveResponseModel> {
  
}
