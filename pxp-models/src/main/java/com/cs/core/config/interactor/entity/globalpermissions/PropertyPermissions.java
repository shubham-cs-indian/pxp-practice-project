package com.cs.core.config.interactor.entity.globalpermissions;

public class PropertyPermissions implements IPropertyPermissions {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected Boolean         isDisabled;
  protected Boolean         canEdit;
  protected Boolean         isHidden;
  protected String          entityId;
  protected String          type;
  
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
  public Boolean getIsDisabled()
  {
    
    return isDisabled;
  }
  
  @Override
  public void setIsDisabled(Boolean isDisabled)
  {
    this.isDisabled = isDisabled;
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
  public String getType()
  {
    
    return type;
  }
  
  @Override
  public void settype(String type)
  {
    this.type = type;
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
  public Boolean getIsHidden()
  {
    return isHidden;
  }
  
  @Override
  public void setIsHidden(Boolean isHidden)
  {
    this.isHidden = isHidden;
  }
}
