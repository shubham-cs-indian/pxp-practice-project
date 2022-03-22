package com.cs.core.runtime.interactor.usecase.notification;

import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IDeleteNotificationsForUser
    extends IRuntimeInteractor<IIdParameterModel, IBulkDeleteReturnModel> {
  
}
