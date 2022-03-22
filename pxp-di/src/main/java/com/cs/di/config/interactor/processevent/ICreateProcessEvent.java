package com.cs.di.config.interactor.processevent;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.processevent.ICreateOrSaveProcessEventResponseModel;
import com.cs.core.config.interactor.model.processevent.IProcessEventModel;

public interface ICreateProcessEvent
    extends ICreateConfigInteractor<IProcessEventModel, ICreateOrSaveProcessEventResponseModel> {
  
}
