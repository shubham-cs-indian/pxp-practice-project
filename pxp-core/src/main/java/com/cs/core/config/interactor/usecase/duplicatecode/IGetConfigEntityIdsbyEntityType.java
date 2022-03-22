package com.cs.core.config.interactor.usecase.duplicatecode;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.runtime.interactor.model.configuration.IGetEntityIdsByEntityTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IGetConfigEntityIdsbyEntityType
    extends IGetConfigInteractor<IGetEntityIdsByEntityTypeModel, IIdsListParameterModel> {
  
}
