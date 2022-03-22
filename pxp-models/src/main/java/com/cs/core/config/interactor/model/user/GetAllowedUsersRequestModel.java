package com.cs.core.config.interactor.model.user;

public class GetAllowedUsersRequestModel implements IGetAllowedUsersRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected Long            from;
  protected Long            size;
  protected String          searchText;
  
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
