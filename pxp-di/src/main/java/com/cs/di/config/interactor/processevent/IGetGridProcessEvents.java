package com.cs.di.config.interactor.processevent;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.di.config.interactor.model.initializeworflowevent.IGetGridProcessEventsResponseModel;

public interface IGetGridProcessEvents
    extends IGetConfigInteractor<IConfigGetAllRequestModel, IGetGridProcessEventsResponseModel> {
  
}
