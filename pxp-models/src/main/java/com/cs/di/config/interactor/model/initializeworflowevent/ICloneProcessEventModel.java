package com.cs.di.config.interactor.model.initializeworflowevent;

import com.cs.core.config.interactor.model.processevent.IProcessEventModel;

public interface ICloneProcessEventModel extends IProcessEventModel {
  
  public static final String ORIGINAL_ENTITY_ID = "originalEntityId";
  
  public String getOriginalEntityId();
  
  public void setOriginalEntityId(String originalEntityId);
}
