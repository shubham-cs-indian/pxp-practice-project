package com.cs.core.config.strategy.usecase.mapping;

import com.cs.core.config.interactor.model.mapping.ICreateOrSaveMappingModel;
import com.cs.core.config.interactor.model.mapping.ISaveMappingModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ISaveMappingStrategy extends IConfigStrategy<ISaveMappingModel, ICreateOrSaveMappingModel> {
  
}
