package com.cs.core.config.interactor.usecase.condition;

import com.cs.config.interactor.usecase.base.IDeleteConfigInteractor;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteCondition
    extends IDeleteConfigInteractor<IIdsListParameterModel, IBulkDeleteReturnModel> {
  
}
