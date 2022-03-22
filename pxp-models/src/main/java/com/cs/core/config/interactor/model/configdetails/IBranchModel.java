package com.cs.core.config.interactor.model.configdetails;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IBranchModel extends IModel {
  
  public String getId();
  
  public void setId(String id);
  
  public String getParent();
  
  public void setParent(String parent);
}
