package com.cs.core.runtime.interactor.model.configuration;

public class UserIdIdsListParameterModel extends IdsListParameterModel
    implements IUserIdIdsListParameterModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          userId;
  
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
}
