package com.cs.core.config.interactor.model.auditlog;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetGridAuditLogUserRequestModel extends IModel{
  
  public static final String FROM        = "from";
  public static final String SIZE        = "size";
  public static final String SORT_ORDER  = "sortOrder";
  public static final String SEARCH_TEXT = "searchText";
  
  public Long getFrom();
  public void setFrom(Long from);
  
  public Long getSize();
  public void setSize(Long size);
  
  public String getSortOrder();
  public void setSortOrder(String sortOrder);
  
  public String getSearchText();
  public void setSearchText(String searchText);
  
}
