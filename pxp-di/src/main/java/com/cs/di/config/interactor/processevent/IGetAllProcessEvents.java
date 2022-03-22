package com.cs.di.config.interactor.processevent;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.di.config.interactor.model.initializeworflowevent.IGetAllProcessEventsResponseModel;

public interface IGetAllProcessEvents
    extends IGetConfigInteractor<IConfigGetAllRequestModel, IGetAllProcessEventsResponseModel> {
  
}
