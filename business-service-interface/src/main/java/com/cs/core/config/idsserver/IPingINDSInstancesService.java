package com.cs.core.config.idsserver;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.dam.config.interactor.model.idsserver.IINDSPingTaskRequestModel;
import com.cs.dam.config.interactor.model.idsserver.IINDSPingTaskResponseModel;

public interface IPingINDSInstancesService extends IGetConfigService<IINDSPingTaskRequestModel, IINDSPingTaskResponseModel> {
  
}
