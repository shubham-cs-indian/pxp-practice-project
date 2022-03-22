package com.cs.core.runtime.interactor.model.configuration;

import java.util.List;

public class IdsListWithUserModel implements IIdsListWithUserModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    ids;
  protected String          userId;
  
  @Override
  public List<String> getIds()
  {
    return ids;
  }
  
  @Override
  public void setIds(List<String> ids)
  {
    this.ids = ids;
  }
  
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
