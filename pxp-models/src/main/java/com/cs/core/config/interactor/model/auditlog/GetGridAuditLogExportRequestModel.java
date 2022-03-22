package com.cs.core.config.interactor.model.auditlog;

import com.cs.core.runtime.interactor.model.instance.IdPaginationModel;

public class GetGridAuditLogExportRequestModel extends IdPaginationModel
    implements IGetGridAuditLogExportRequestModel {
  
  private static final long serialVersionUID = 1L;
  
  protected Long            from;
  protected Long            size;
  protected String          sortBy;
  protected String          sortOrder;
  protected String          userName;
  
  @Override
  public Long getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(Long from)
  {
    this.from = from;
  }
  
  @Override
  public Long getSize()
  {
    return size;
  }
  
  @Override
  public void setSize(Long size)
  {
    this.size = size;
  }
  
  @Override
  public String getSortBy()
  {
    return sortBy;
  }
  
  @Override
  public void setSortBy(String sortBy)
  {
    this.sortBy = sortBy;
  }
  
  @Override
  public String getSortOrder()
  {
    return sortOrder;
  }
  
  @Override
  public void setSortOrder(String sortOrder)
  {
    this.sortOrder = sortOrder;
  }
  
  @Override
  public String getUserName()
  {
    return userName;
  }
  
  @Override
  public void setUserName(String userName)
  {
    this.userName = userName;
  }
  
}
