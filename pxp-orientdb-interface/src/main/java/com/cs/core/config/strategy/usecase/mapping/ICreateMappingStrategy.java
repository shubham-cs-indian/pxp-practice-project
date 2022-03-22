package com.cs.core.config.strategy.usecase.mapping;

import com.cs.core.config.interactor.model.mapping.ICreateOrSaveMappingModel;
import com.cs.core.config.interactor.model.mapping.IMappingModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ICreateMappingStrategy extends IConfigStrategy<IMappingModel, ICreateOrSaveMappingModel> {
  
}
