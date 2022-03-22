package com.cs.core.config.interactor.usecase.user;

import com.cs.config.interactor.usecase.base.IConfigInteractor;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteUser
    extends IConfigInteractor<IIdsListParameterModel, IBulkDeleteReturnModel> {
  
}
