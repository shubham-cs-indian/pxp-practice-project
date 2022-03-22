package com.cs.core.config.strategy.usecase.notification;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.notification.IGetConfigDetailsForNotificationModel;

public interface IGetConfigDetailsForNotificationStrategy
    extends IConfigStrategy<IListModel<IIdAndTypeModel>, IGetConfigDetailsForNotificationModel> {
  
}
