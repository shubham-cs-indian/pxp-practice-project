package com.cs.di.config.interactor.authorization;

import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.di.base.IDiInteractor;

public interface IDeletePartnerAuthorization extends IDiInteractor<IIdsListParameterModel, IBulkDeleteReturnModel> {
  
}
