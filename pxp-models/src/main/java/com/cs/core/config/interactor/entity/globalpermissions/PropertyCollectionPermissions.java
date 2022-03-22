package com.cs.core.config.interactor.entity.globalpermissions;

public class PropertyCollectionPermissions implements IPropertyCollectionPermissions {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected Boolean         isHidden;
  protected Boolean         isCollapsed;
  protected Boolean         canEdit;
  protected String          entityId;
  
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
  public Boolean getIsHidden()
  {
    
    return isHidden;
  }
  
  @Override
  public void setIsHidden(Boolean isHidden)
  {
    this.isHidden = isHidden;
  }
  
  @Override
  public Boolean getIsCollapsed()
  {
    
    return isCollapsed;
  }
  
  @Override
  public void setIsCollapsed(Boolean isCollapsed)
  {
    this.isCollapsed = isCollapsed;
  }
  
  @Override
  public Boolean getCanEdit()
  {
    
    return canEdit;
  }
  
  @Override
  public void setCanEdit(Boolean canEdit)
  {
    this.canEdit = canEdit;
  }
  
  @Override
  public String getEntityId()
  {
    
    return entityId;
  }
  
  @Override
  public void setEntityId(String entityId)
  {
    this.entityId = entityId;
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
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
}
