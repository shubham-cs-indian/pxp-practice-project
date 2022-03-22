package com.cs.di.config.businessapi.processevent;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.di.config.interactor.model.initializeworflowevent.IGetAllProcessEventsResponseModel;

public interface IGetAllProcessEventsService extends IGetConfigService<IConfigGetAllRequestModel, IGetAllProcessEventsResponseModel> {
  
}
