package com.cs.dam.config.strategy.usecase.idsserver;

import com.cs.dam.config.interactor.model.idsserver.IINDSConfigurationTaskRequestModel;
import com.cs.dam.config.interactor.model.idsserver.IINDSPingTaskResponseModel;

public interface IPerformINDSConfigurationTaskStrategy extends
    IInDesignServerStrategy<IINDSConfigurationTaskRequestModel, IINDSPingTaskResponseModel> {
  
}
