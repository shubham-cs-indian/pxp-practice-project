package com.cs.di.config.businessapi.processevent;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.processevent.ICreateOrSaveProcessEventResponseModel;
import com.cs.di.config.interactor.model.initializeworflowevent.ISaveProcessEventModel;

public interface ISaveProcessEventService extends ISaveConfigService<ISaveProcessEventModel, ICreateOrSaveProcessEventResponseModel> {
  
}
