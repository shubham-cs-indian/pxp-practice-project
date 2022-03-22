package com.cs.core.runtime.interactor.model.user;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ICurrentUserIdAndTypesModel extends IModel {
  
  public static String CURRENT_USER_ID = "currentUserId";
  public static String SECONDARY_TYPES = "secondaryTypes";
  
  public String getCurrentUserId();
  
  public void setCurrentUserId(String currentUserId);
  
  public List<String> getSecondaryTypes();
  
  public void setSecondaryTypes(List<String> secondaryTypes);
}
