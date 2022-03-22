package com.cs.core.config.interactor.usecase.events;

import com.cs.core.runtime.interactor.model.configuration.IEventModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetEvents extends IRuntimeInteractor<IIdParameterModel, IEventModel> {
  
}
