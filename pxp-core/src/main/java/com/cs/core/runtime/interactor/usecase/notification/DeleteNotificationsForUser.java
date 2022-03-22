package com.cs.core.runtime.interactor.usecase.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.notification.IDeleteNotificationsForUserService;

@Service
public class DeleteNotificationsForUser extends AbstractRuntimeInteractor<IIdParameterModel, IBulkDeleteReturnModel>
    implements IDeleteNotificationsForUser {
  
  @Autowired
  protected IDeleteNotificationsForUserService deleteNotificationsForUserService;
  
  @Override
  public IBulkDeleteReturnModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return deleteNotificationsForUserService.execute(dataModel);
  }
}
