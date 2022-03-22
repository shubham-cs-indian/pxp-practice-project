package com.cs.core.runtime.interactor.model.configuration;

import com.cs.core.runtime.interactor.model.filter.ISortModel;

import java.util.List;

public interface IGetAllModel extends IModel {
  
  public static String FROM          = "from";
  public static String SIZE          = "size";
  public static String SEARCH_STRING = "searchString";
  public static String SORT_OPTIONS  = "sortOptions";
  
  public Integer getFrom();
  
  public void setFrom(Integer from);
  
  public Integer getSize();
  
  public void setSize(Integer size);
  
  public String getSearchString();
  
  public void setSearchString(String searchString);
  
  public List<ISortModel> getSortOptions();
  
  public void setSortOptions(List<ISortModel> sortOptions);
}
