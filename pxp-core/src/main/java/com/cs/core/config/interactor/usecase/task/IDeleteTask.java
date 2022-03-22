package com.cs.core.config.interactor.usecase.task;

import com.cs.config.interactor.usecase.base.IDeleteConfigInteractor;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteTask
    extends IDeleteConfigInteractor<IIdsListParameterModel, IBulkDeleteReturnModel> {
  
}
