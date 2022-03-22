package com.cs.dam.config.strategy.usecase.dtp.idsserver;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.dam.config.interactor.model.idsserver.IINDSPingTaskRequestModel;

public interface IGetAllInDesignServerInstancesStrategy
    extends IConfigStrategy<IModel, IINDSPingTaskRequestModel> {
  
}
