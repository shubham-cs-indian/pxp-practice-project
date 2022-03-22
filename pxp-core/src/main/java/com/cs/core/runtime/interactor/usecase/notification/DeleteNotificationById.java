package com.cs.core.runtime.interactor.usecase.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.notification.IDeleteNotificationByIdService;

@Service
public class DeleteNotificationById extends AbstractRuntimeInteractor<IIdParameterModel, IIdParameterModel>
    implements IDeleteNotificationById {
  
  @Autowired
  protected IDeleteNotificationByIdService deleteNotificationByIdService;
  
  @Override
  public IIdParameterModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return deleteNotificationByIdService.execute(dataModel);
  }
}
