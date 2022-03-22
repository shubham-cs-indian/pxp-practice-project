package com.cs.di.config.strategy.usecase.processevent;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.di.config.interactor.model.initializeworflowevent.IGetGridProcessEventsResponseModel;

public interface IGetGridProcessEventsStrategy
    extends IConfigStrategy<IConfigGetAllRequestModel, IGetGridProcessEventsResponseModel> {
  
}
