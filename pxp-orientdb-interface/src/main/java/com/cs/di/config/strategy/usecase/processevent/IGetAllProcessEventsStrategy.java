package com.cs.di.config.strategy.usecase.processevent;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.di.config.interactor.model.initializeworflowevent.IGetAllProcessEventsResponseModel;

public interface IGetAllProcessEventsStrategy
    extends IConfigStrategy<IConfigGetAllRequestModel, IGetAllProcessEventsResponseModel> {
  
}
