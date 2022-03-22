package com.cs.di.config.interactor.mappings;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.mapping.ICreateOutBoundMappingResponseModel;
import com.cs.core.config.interactor.model.mapping.ISaveOutBoundMappingModel;

public interface ISaveOutBoundMapping
    extends ISaveConfigInteractor<ISaveOutBoundMappingModel, ICreateOutBoundMappingResponseModel> {
  
}
