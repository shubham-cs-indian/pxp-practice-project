package com.cs.core.runtime.interactor.model.configuration;

public interface IUserIdIdsListParameterModel extends IIdsListParameterModel {
  
  public static String USER_ID = "userId";
  
  public String getUserId();
  
  public void setUserId(String userId);
}
