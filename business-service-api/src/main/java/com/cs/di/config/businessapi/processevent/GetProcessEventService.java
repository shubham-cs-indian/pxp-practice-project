package com.cs.di.config.businessapi.processevent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.processevent.IGetProcessEventModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.di.config.strategy.usecase.processevent.IGetProcessEventStrategy;

@Service
public class GetProcessEventService extends AbstractGetConfigService<IIdParameterModel, IGetProcessEventModel>
    implements IGetProcessEventService {
  
  @Autowired
  protected IGetProcessEventStrategy getProcessEventStrategy;
  
  @Override
  public IGetProcessEventModel executeInternal(IIdParameterModel processEventModel) throws Exception
  {
    return getProcessEventStrategy.execute(processEventModel);
  }
}
