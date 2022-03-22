package com.cs.api.estordbmsmigration.model.migration;

import com.cs.core.runtime.interactor.model.configuration.IModel;


public interface IConfigDataForHelperTablesModel extends IModel {
  
  public void setCid(String cid);
  public String getCid();
  
  public void setIId(long iid);
  public long getIId();
  
  public void setCode(String code);
  public String getCode();
  
  public void setType(String type);
  public String getType();
  
  public void setIsNature(boolean isNature);
  public boolean getIsNature();
  
  public void setUserName(String userName);
  public String getUserName();
  
  public void setSide1ElementId(String side1ElementId);
  public String getSide1ElementId();
  
  public void setSide2ElementId(String side2ElementId);
  public String getSide2ElementId();
}
