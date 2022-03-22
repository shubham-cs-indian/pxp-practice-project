package com.cs.core.config.interactor.model.template;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IModifiedSequenceModel extends IModel {
  
  public static final String ID       = "id";
  public static final String SEQUENCE = "sequence";
  
  public String getId();
  
  public void setId(String id);
  
  public Integer getSequence();
  
  public void setSequence(Integer sequence);
}
