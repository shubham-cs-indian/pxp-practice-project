package com.cs.core.runtime.interactor.usecase.notification;

import com.cs.core.runtime.interactor.model.configuration.IGetAllModel;
import com.cs.core.runtime.interactor.model.notification.IGetAllNotificationsResponseModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetAllNotificationsForUser
    extends IRuntimeInteractor<IGetAllModel, IGetAllNotificationsResponseModel> {
  
}
