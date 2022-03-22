package com.cs.di.config.interactor.mappings;

import com.cs.config.interactor.usecase.base.IDeleteConfigInteractor;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteMapping
    extends IDeleteConfigInteractor<IIdsListParameterModel, IBulkDeleteReturnModel> {
  
}
