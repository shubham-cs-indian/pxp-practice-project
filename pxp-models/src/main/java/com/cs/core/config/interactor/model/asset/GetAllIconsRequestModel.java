package com.cs.core.config.interactor.model.asset;


public class GetAllIconsRequestModel implements IGetAllIconsRequestModel{

  private static final long serialVersionUID = 1L;
  
  protected Long            from;
  protected Long            size;
  protected String          searchText;
  protected String          idToExclude;
  
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

  @Override
  public String getIdToExclude()
  {
    return idToExclude;
  }

  @Override
  public void setIdToExclude(String idToExclude)
  {
    this.idToExclude = idToExclude;
  }
  
  
}
