package com.cs.core.runtime.interactor.entity.configuration.base;

public class SortEntity implements ISortEntity {
  
  private static final long serialVersionUID = 1L;
  protected String          sortField;
  protected String          sortOrder;
  protected Boolean         isNumeric;
  
  @Override
  public String getSortField()
  {
    return sortField;
  }
  
  @Override
  public void setSortField(String sortField)
  {
    this.sortField = sortField;
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
  public Boolean getIsNumeric()
  {
    return isNumeric;
  }
  
  @Override
  public void setIsNumeric(Boolean isNumeric)
  {
    this.isNumeric = isNumeric;
  }
  
  @Override
  public String getId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setId(String id)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
}
