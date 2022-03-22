package com.cs.core.config.interactor.model.auditlog;

import com.cs.core.runtime.interactor.model.instance.IdPaginationModel;

public class GetGridAuditLogUserRequestModel extends IdPaginationModel implements IGetGridAuditLogUserRequestModel{

  private static final long serialVersionUID = 1L;
  protected Long            from;
  protected Long            size;
  protected String          searchText;
  protected String          sortOrder;
  
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
  public String getSearchText()
  {
    return searchText;
  }
  
  @Override
  public void setSearchText(String searchText)
  {
    this.searchText = searchText;
  }
}
