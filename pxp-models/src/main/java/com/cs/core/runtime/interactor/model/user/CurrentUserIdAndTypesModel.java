package com.cs.core.runtime.interactor.model.user;

import java.util.ArrayList;
import java.util.List;

public class CurrentUserIdAndTypesModel implements ICurrentUserIdAndTypesModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          currentUserId;
  protected List<String>    secondaryTypes;
  
  @Override
  public String getCurrentUserId()
  {
    return currentUserId;
  }
  
  @Override
  public void setCurrentUserId(String currentUserId)
  {
    this.currentUserId = currentUserId;
  }
  
  @Override
  public List<String> getSecondaryTypes()
  {
    if (secondaryTypes == null) {
      secondaryTypes = new ArrayList<>();
    }
    return secondaryTypes;
  }
  
  @Override
  public void setSecondaryTypes(List<String> secondaryTypes)
  {
    this.secondaryTypes = secondaryTypes;
  }
}
