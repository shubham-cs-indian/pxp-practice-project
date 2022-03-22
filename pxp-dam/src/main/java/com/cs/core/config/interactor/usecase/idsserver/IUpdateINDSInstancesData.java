package com.cs.core.config.interactor.usecase.idsserver;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.dam.config.interactor.model.idsserver.IINDSConfigurationTaskRequestModel;
import com.cs.dam.config.interactor.model.idsserver.IINDSPingTaskResponseModel;

public interface IUpdateINDSInstancesData extends ISaveConfigInteractor<IINDSConfigurationTaskRequestModel, IINDSPingTaskResponseModel> {
  
}
