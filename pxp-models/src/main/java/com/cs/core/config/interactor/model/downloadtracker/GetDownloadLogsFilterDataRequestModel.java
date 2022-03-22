package com.cs.core.config.interactor.model.downloadtracker;

public class GetDownloadLogsFilterDataRequestModel implements IGetDownloadLogsFilterDataRequestModel {

  private static final long serialVersionUID = 1L;
  protected Long            from;
  protected Long            size;
  protected String          searchText;
  protected String          sortOrder;
  protected String          columnName;

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

  @Override
  public String getColumnName()
  {
    return columnName;
  }

  @Override
  public void setColumnName(String columnName)
  {
    this.columnName = columnName;
  }
}
