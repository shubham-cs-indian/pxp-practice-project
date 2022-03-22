package com.cs.core.runtime.notification;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.configuration.IGetAllModel;
import com.cs.core.runtime.interactor.model.notification.IGetAllNotificationsResponseModel;

public interface IGetAllNotificationsForUserService extends IRuntimeService<IGetAllModel, IGetAllNotificationsResponseModel> {
  
}
