package com.cs.di.config.businessapi.processevent;

import com.cs.config.businessapi.base.IDeleteConfigService;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.di.config.interactor.model.initializeworflowevent.IDeleteProcessEventResponseModel;

public interface IDeleteProcessEventService
    extends IDeleteConfigService<IIdsListParameterModel, IDeleteProcessEventResponseModel> {
  
}
