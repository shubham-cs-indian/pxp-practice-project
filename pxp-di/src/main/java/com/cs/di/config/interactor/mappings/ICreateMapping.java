package com.cs.di.config.interactor.mappings;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.mapping.ICreateOrSaveMappingModel;
import com.cs.core.config.interactor.model.mapping.IMappingModel;

public interface ICreateMapping extends ICreateConfigInteractor<IMappingModel, ICreateOrSaveMappingModel> {
  
}
