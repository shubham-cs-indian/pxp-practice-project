package com.cs.dam.config.strategy.usecase.duplicatedetection;

import com.cs.core.config.interactor.model.attribute.IBooleanReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetDuplicateDetectionStatusStrategy
    extends IRuntimeStrategy<IVoidModel, IBooleanReturnModel> {
  
}
