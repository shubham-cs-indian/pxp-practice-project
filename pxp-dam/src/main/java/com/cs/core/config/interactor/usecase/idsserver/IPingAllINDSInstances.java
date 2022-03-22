package com.cs.core.config.interactor.usecase.idsserver;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.dam.config.interactor.model.idsserver.IINDSPingTaskResponseModel;

public interface IPingAllINDSInstances extends IGetConfigInteractor<IModel, IINDSPingTaskResponseModel> {
  
}
