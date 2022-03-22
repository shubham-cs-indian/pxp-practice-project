package com.cs.core.config.interactor.model.processevent;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.processevent.ProcessEvent;

public class ProcessEventModel extends ProcessEvent implements IProcessEventModel {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public IEntity getEntity()
  {
    return null;
  }
}
