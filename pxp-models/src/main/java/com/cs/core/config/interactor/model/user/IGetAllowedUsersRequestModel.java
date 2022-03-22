package com.cs.core.config.interactor.model.user;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetAllowedUsersRequestModel extends IModel {
  
  public static final String FROM        = "from";
  public static final String SIZE        = "size";
  public static final String SEARCH_TEXT = "searchText";
  
  public Long getFrom();
  
  public void setFrom(Long from);
  
  public Long getSize();
  
  public void setSize(Long size);
  
  public String getSearchText();
  
  public void setSearchText(String searchText);
}
