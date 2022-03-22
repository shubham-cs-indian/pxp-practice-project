package com.cs.di.config.interactor.mappings;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.mapping.IGetOutAndInboundMappingModel;
import com.cs.core.config.interactor.model.mapping.IOutBoundMappingModel;

public interface IGetOutBoundMapping
    extends IGetConfigInteractor<IGetOutAndInboundMappingModel, IOutBoundMappingModel> {
  
}
