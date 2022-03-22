package com.cs.core.config.interactor.usecase.idsserver;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.dam.config.interactor.model.idsserver.IINDSPingTaskRequestModel;
import com.cs.dam.config.interactor.model.idsserver.IINDSPingTaskResponseModel;

public interface IPingINDSInstances extends IGetConfigInteractor<IINDSPingTaskRequestModel, IINDSPingTaskResponseModel> {
  
}
