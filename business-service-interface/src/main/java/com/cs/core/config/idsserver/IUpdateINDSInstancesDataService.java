package com.cs.core.config.idsserver;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.dam.config.interactor.model.idsserver.IINDSConfigurationTaskRequestModel;
import com.cs.dam.config.interactor.model.idsserver.IINDSPingTaskResponseModel;

public interface IUpdateINDSInstancesDataService extends ISaveConfigService<IINDSConfigurationTaskRequestModel, IINDSPingTaskResponseModel> {
  
}
