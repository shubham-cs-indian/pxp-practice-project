package com.cs.di.config.interactor.processevent;

import com.cs.config.interactor.usecase.base.IConfigInteractor;
import com.cs.core.config.interactor.model.processevent.ICreateOrSaveProcessEventResponseModel;
import com.cs.di.config.interactor.model.initializeworflowevent.ISaveProcessEventModel;

public interface ISaveProcessEvent
    extends IConfigInteractor<ISaveProcessEventModel, ICreateOrSaveProcessEventResponseModel> {
  
}
