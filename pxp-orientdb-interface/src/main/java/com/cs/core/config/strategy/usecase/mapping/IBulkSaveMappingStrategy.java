package com.cs.core.config.strategy.usecase.mapping;

import com.cs.core.config.interactor.model.mapping.IBulkSaveMappingModel;
import com.cs.core.config.interactor.model.mapping.IBulkSaveMappingResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IBulkSaveMappingStrategy
    extends IConfigStrategy<IBulkSaveMappingModel, IBulkSaveMappingResponseModel> {
  
}
