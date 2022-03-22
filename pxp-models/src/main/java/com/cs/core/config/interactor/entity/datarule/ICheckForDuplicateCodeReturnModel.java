package com.cs.core.config.interactor.entity.datarule;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ICheckForDuplicateCodeReturnModel extends IModel {
  
  public static final String FOUND = "found";
  
  public Boolean getFound();
  
  public void setFound(Boolean found);
}
