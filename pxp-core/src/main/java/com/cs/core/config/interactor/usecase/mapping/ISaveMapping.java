package com.cs.core.config.interactor.usecase.mapping;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.mapping.ICreateOrSaveMappingModel;
import com.cs.core.config.interactor.model.mapping.ISaveMappingModel;

public interface ISaveMapping extends ISaveConfigInteractor<ISaveMappingModel, ICreateOrSaveMappingModel> {
  
}
