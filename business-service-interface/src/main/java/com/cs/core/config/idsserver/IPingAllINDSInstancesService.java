package com.cs.core.config.idsserver;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.dam.config.interactor.model.idsserver.IINDSPingTaskResponseModel;

public interface IPingAllINDSInstancesService extends IGetConfigService<IModel, IINDSPingTaskResponseModel> {
  
}
