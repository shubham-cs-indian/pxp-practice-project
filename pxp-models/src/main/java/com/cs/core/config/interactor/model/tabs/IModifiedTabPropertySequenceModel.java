package com.cs.core.config.interactor.model.tabs;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IModifiedTabPropertySequenceModel extends IModel {
  
  public static final String ID           = "id";
  public static final String NEW_SEQUENCE = "newSequence";
  
  public String getId();
  
  public void setId(String id);
  
  public Integer getNewSequence();
  
  public void setNewSequence(Integer newSequence);
}
