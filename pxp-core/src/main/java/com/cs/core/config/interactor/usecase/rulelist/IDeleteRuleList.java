package com.cs.core.config.interactor.usecase.rulelist;

import com.cs.config.interactor.usecase.base.IDeleteConfigInteractor;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteRuleList
    extends IDeleteConfigInteractor<IIdsListParameterModel, IBulkDeleteReturnModel> {
  
}
