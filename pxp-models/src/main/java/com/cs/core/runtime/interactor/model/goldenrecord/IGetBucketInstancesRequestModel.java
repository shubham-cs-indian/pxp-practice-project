package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetBucketInstancesRequestModel extends IModel {
  
  public static String ID          = "id";
  public static String FROM        = "from";
  public static String SIZE        = "size";
  public static String SEARCH_TEXT = "searchText";
  
  public String getId();
  
  public void setId(String id);
  
  public Integer getFrom();
  
  public void setFrom(Integer from);
  
  public Integer getSize();
  
  public void setSize(Integer size);
  
  public String getSearchText();
  
  public void setSearchText(String searchText);
}
