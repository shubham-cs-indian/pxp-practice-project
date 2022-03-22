package com.cs.core.config.interactor.model.auditlog;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetGridAuditLogExportRequestModel extends IModel {
  
  public static final String FROM       = "from";
  public static final String SIZE       = "size";
  public static final String SORT_BY    = "sortBy";
  public static final String SORT_ORDER = "sortOrder";
  
  public static final String USERNAME   = "userName";

  public void setUserName(String userName);
  public String getUserName();
  
  public String getSortOrder();
  public void setSortOrder(String sortOrder);
  
  public void setSortBy(String sortBy);
  public String getSortBy();
  
  public Long getFrom();
  public void setFrom(Long from);
  
  public Long getSize();
  public void setSize(Long size);
}
