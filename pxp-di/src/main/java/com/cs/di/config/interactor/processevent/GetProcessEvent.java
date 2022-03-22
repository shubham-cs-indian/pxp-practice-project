package com.cs.di.config.interactor.processevent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.processevent.IGetProcessEventModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.di.config.businessapi.processevent.IGetProcessEventService;

@Service
public class GetProcessEvent extends AbstractGetConfigInteractor<IIdParameterModel, IGetProcessEventModel> implements IGetProcessEvent {
  
  @Autowired
  protected IGetProcessEventService getProcessEventService;
  
  @Override
  public IGetProcessEventModel executeInternal(IIdParameterModel processEventModel) throws Exception
  {
    return getProcessEventService.execute(processEventModel);
  }
}
