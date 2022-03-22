package com.cs.core.config.interactor.model.downloadtracker;

import com.cs.core.runtime.interactor.model.configuration.IModel;


public interface IGetDownloadLogsFilterDataRequestModel extends IModel {

  public static final String FROM        = "from";
  public static final String SIZE        = "size";
  public static final String SORT_ORDER  = "sortOrder";
  public static final String SEARCH_TEXT = "searchText";
  public static final String COLUMN_NAME = "columnName";

  public Long getFrom();
  public void setFrom(Long from);

  public Long getSize();
  public void setSize(Long size);

  public String getSortOrder();
  public void setSortOrder(String sortOrder);

  public String getSearchText();
  public void setSearchText(String searchText);

  public String getColumnName();
  public void setColumnName(String columnName);
}
