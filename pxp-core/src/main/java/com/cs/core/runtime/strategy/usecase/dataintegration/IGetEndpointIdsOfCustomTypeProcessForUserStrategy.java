package com.cs.core.runtime.strategy.usecase.dataintegration;

import com.cs.core.config.interactor.model.processevent.IGetProcessExportEndpointModel;
import com.cs.core.runtime.interactor.model.dataintegration.IGetEndpointIdsRequestModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetEndpointIdsOfCustomTypeProcessForUserStrategy
    extends IRuntimeStrategy<IGetEndpointIdsRequestModel, IGetProcessExportEndpointModel> {
  
}
