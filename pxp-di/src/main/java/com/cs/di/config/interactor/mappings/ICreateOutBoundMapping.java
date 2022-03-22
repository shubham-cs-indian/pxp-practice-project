package com.cs.di.config.interactor.mappings;

import com.cs.config.interactor.usecase.base.IConfigInteractor;
import com.cs.core.config.interactor.model.mapping.ICreateOutBoundMappingResponseModel;
import com.cs.core.config.interactor.model.mapping.IOutBoundMappingModel;

public interface ICreateOutBoundMapping
    extends IConfigInteractor<IOutBoundMappingModel, ICreateOutBoundMappingResponseModel> {
  
}
