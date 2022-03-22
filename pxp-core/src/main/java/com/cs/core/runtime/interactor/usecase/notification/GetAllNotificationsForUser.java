package com.cs.core.runtime.interactor.usecase.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.configuration.IGetAllModel;
import com.cs.core.runtime.interactor.model.notification.IGetAllNotificationsResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.notification.IGetAllNotificationsForUserService;

@Service
public class GetAllNotificationsForUser extends AbstractRuntimeInteractor<IGetAllModel, IGetAllNotificationsResponseModel>
    implements IGetAllNotificationsForUser {
  
  @Autowired
  protected IGetAllNotificationsForUserService getAllNotificationsForUserService;
  
  @Override
  public IGetAllNotificationsResponseModel executeInternal(IGetAllModel dataModel) throws Exception
  {
    return getAllNotificationsForUserService.execute(dataModel);
  }
  
}