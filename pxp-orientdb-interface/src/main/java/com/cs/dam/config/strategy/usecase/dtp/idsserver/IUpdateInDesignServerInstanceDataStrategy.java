package com.cs.dam.config.strategy.usecase.dtp.idsserver;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.dam.config.interactor.model.idsserver.IINDSConfigurationTaskRequestModel;
import com.cs.dam.config.interactor.model.idsserver.IINDSConfigurationTaskResponseModel;

public interface IUpdateInDesignServerInstanceDataStrategy extends
    IConfigStrategy<IINDSConfigurationTaskRequestModel, IINDSConfigurationTaskResponseModel> {
  
}
