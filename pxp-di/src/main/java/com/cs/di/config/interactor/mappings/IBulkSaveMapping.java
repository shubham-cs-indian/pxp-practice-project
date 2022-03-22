package com.cs.di.config.interactor.mappings;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.mapping.IBulkSaveMappingModel;
import com.cs.core.config.interactor.model.mapping.IBulkSaveMappingResponseModel;

public interface IBulkSaveMapping
    extends ISaveConfigInteractor<IBulkSaveMappingModel, IBulkSaveMappingResponseModel> {
  
}
