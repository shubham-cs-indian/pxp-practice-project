package com.cs.core.config.interactor.model.asset;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetAllIconsRequestModel extends IModel {
  
  public static final String FROM          = "from";
  public static final String SIZE          = "size";
  public static final String SEARCH_TEXT   = "searchText";
  public static final String ID_TO_EXCLUDE = "idToExclude";
  
  public Long getFrom();
  public void setFrom(Long from);
  
  public Long getSize();
  public void setSize(Long size);
  
  public String getSearchText();
  public void setSearchText(String searchText);
  
  public String getIdToExclude();
  public void setIdToExclude(String idToExclude);
}
