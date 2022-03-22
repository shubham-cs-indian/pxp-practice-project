package com.cs.core.runtime.interactor.model.configuration;

import java.util.List;

public interface IIdsListWithUserModel extends IModel {
  
  public static String IDS     = "ids";
  public static String USER_ID = "userId";
  
  public List<String> getIds();
  
  public void setIds(List<String> ids);
  
  public String getUserId();
  
  public void setUserId(String userId);
}
