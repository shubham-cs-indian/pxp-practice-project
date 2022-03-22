package com.cs.core.config.interactor.model.tag;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetTagModel extends IModel {
  
  public String getMode();
  
  public void setMode(String mode);
  
  public String getId();
  
  public void setId(String id);
}
