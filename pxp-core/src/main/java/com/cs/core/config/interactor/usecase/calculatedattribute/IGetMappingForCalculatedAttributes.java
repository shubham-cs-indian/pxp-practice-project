package com.cs.core.config.interactor.usecase.calculatedattribute;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.calculatedattribute.ICalculatedAttributeMappingModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetMappingForCalculatedAttributes
    extends IGetConfigInteractor<IModel, ICalculatedAttributeMappingModel> {
  
}
