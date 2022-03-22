package com.cs.api.estordbmsmigration.model.migration;

import java.util.List;
import java.util.Set;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ISyncCongifToRDBMSModel extends IModel {
  
  public void setCode(String code);
  
  public String getCode();
  
  public void setType(String type);
  
  public String getType();
  
  public void setChildrens(List<ISyncCongifToRDBMSModel> childrens);
  
  public List<ISyncCongifToRDBMSModel> getChildrens();
  
  public void setUserName(String userName);
  
  public String getUserName();
  
}
