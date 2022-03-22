package com.cs.core.config.interactor.model.endpoint;

import com.cs.core.config.interactor.model.processevent.ProcessEventModel;
import com.cs.di.config.interactor.model.initializeworflowevent.ICloneProcessEventModel;

public class CloneProcessEventModel extends ProcessEventModel implements ICloneProcessEventModel {
  
  private static final long serialVersionUID = 1L;
  private String            originalEntityId;
  
  @Override
  public String getOriginalEntityId()
  {
    return originalEntityId;
  }
  
  @Override
  public void setOriginalEntityId(String originalEntityId)
  {
    this.originalEntityId = originalEntityId;
  }
}
