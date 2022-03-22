package com.cs.di.config.interactor.processevent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.processevent.ICreateOrSaveProcessEventResponseModel;
import com.cs.core.config.interactor.model.processevent.IProcessEventModel;
import com.cs.di.config.businessapi.processevent.ICreateProcessEventService;

@Service
public class CreateProcessEvent extends AbstractCreateConfigInteractor<IProcessEventModel, ICreateOrSaveProcessEventResponseModel>
    implements ICreateProcessEvent {
  
  @Autowired
  protected ICreateProcessEventService createProcessEventService;
  
  @Override
  public ICreateOrSaveProcessEventResponseModel executeInternal(IProcessEventModel processEventModel) throws Exception
  {
    return createProcessEventService.execute(processEventModel);
  }
}
