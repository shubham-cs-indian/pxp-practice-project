package com.cs.di.config.interactor.processevent;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.processevent.IGetProcessEventModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetProcessEvent
    extends IGetConfigInteractor<IIdParameterModel, IGetProcessEventModel> {
  
}
