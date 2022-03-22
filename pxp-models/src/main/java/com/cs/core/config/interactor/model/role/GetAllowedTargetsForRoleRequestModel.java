package com.cs.core.config.interactor.model.role;

public class GetAllowedTargetsForRoleRequestModel implements IGetAllowedTargetsForRoleRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          selectionType;
  protected String          id;
  protected Long            from;
  protected Long            size;
  protected String          searchText;
  protected String          organizationId;
  protected String          searchColumn;
  
  @Override
  public String getSelectionType()
  {
    return selectionType;
  }
  
  @Override
  public void setSelectionType(String selectionType)
  {
    this.selectionType = selectionType;
  }
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
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
  public String getOrganizationId()
  {
    return organizationId;
  }
  
  @Override
  public void setOrganizationId(String organizationId)
  {
    this.organizationId = organizationId;
  }
  
  @Override
  public String getSearchColumn()
  {
    return searchColumn;
  }
  
  @Override
  public void setSearchColumn(String searchColumn)
  {
    this.searchColumn = searchColumn;
  }
}
