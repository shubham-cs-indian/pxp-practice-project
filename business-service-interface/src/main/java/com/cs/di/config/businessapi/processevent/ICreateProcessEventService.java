package com.cs.di.config.businessapi.processevent;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.processevent.ICreateOrSaveProcessEventResponseModel;
import com.cs.core.config.interactor.model.processevent.IProcessEventModel;

public interface ICreateProcessEventService extends ICreateConfigService<IProcessEventModel, ICreateOrSaveProcessEventResponseModel> {
  
}
