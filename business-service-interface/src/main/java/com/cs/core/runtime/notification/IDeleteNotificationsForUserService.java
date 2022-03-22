package com.cs.core.runtime.notification;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IDeleteNotificationsForUserService extends IRuntimeService<IIdParameterModel, IBulkDeleteReturnModel> {
  
}
