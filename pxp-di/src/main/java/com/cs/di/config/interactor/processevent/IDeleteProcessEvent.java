package com.cs.di.config.interactor.processevent;

import com.cs.config.interactor.usecase.base.IDeleteConfigInteractor;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.di.config.interactor.model.initializeworflowevent.IDeleteProcessEventResponseModel;

public interface IDeleteProcessEvent
    extends IDeleteConfigInteractor<IIdsListParameterModel, IDeleteProcessEventResponseModel> {
  
}
