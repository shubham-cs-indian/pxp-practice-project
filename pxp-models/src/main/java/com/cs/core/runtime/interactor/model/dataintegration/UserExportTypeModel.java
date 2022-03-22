package com.cs.core.runtime.interactor.model.dataintegration;

public class UserExportTypeModel implements IUserExportTypeModel {
  
  private static final long serialVersionUID = 1L;
  private String            userId;
  private String            type;
  
  @Override
  public String getUserId()
  {
    return userId;
  }
  
  @Override
  public void setUserId(String userId)
  {
    this.userId = userId;
  }
  
  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
}
