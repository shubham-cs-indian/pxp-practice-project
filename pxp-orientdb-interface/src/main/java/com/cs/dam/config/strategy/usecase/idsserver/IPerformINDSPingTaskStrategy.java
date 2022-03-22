package com.cs.dam.config.strategy.usecase.idsserver;

import com.cs.dam.config.interactor.model.idsserver.IINDSPingTaskRequestModel;
import com.cs.dam.config.interactor.model.idsserver.IINDSPingTaskResponseModel;

public interface IPerformINDSPingTaskStrategy
    extends IInDesignServerStrategy<IINDSPingTaskRequestModel, IINDSPingTaskResponseModel> {
  
}
