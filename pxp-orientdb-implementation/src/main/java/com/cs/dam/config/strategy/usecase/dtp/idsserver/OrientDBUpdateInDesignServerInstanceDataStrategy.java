package com.cs.dam.config.strategy.usecase.dtp.idsserver;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.dam.config.interactor.model.idsserver.IINDSConfigurationTaskRequestModel;
import com.cs.dam.config.interactor.model.idsserver.IINDSConfigurationTaskResponseModel;
import com.cs.dam.config.interactor.model.idsserver.INDSConfigurationTaskResponseModel;

@Component("updateInDesignServerInstanceDataStrategy")
public class OrientDBUpdateInDesignServerInstanceDataStrategy extends OrientDBBaseStrategy
    implements IUpdateInDesignServerInstanceDataStrategy {
  
  @Override
  public IINDSConfigurationTaskResponseModel execute(IINDSConfigurationTaskRequestModel requestModel)
      throws Exception
  {
    return execute(UPDATE_INDS_INSTANCE_DATA, requestModel, INDSConfigurationTaskResponseModel.class);
  }
  
}
