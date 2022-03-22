package com.cs.core.runtime.interactor.usecase.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.notification.IChangeNotificationStatusService;

@Service
public class ChangeNotificationStatus extends AbstractRuntimeInteractor<IIdsListParameterModel, IIdsListParameterModel>
    implements IChangeNotificationStatus {
  
  @Autowired
  protected IChangeNotificationStatusService changeNotificationStatusService;
  
  @Override
  public IIdsListParameterModel executeInternal(IIdsListParameterModel dataModel) throws Exception
  {
    return changeNotificationStatusService.execute(dataModel);
  }
}
