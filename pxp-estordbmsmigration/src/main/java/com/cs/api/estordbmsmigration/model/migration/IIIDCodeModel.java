package com.cs.api.estordbmsmigration.model.migration;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IIIDCodeModel extends IModel {
  
  public void setIID(Long iid);
  
  public Long getIID();
  
  public void setCode(String code);
  
  public String getCode();
  
}
