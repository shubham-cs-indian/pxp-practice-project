package com.cs.di.config.strategy.processevent;

import com.cs.core.config.interactor.model.processevent.ICreateOrSaveProcessEventResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.di.config.interactor.model.initializeworflowevent.ISaveProcessEventModel;

public interface ISaveProcessEventStrategy
    extends IConfigStrategy<ISaveProcessEventModel, ICreateOrSaveProcessEventResponseModel> {
  
}
