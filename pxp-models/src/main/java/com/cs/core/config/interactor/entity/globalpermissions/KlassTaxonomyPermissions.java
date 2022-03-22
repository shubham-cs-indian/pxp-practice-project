package com.cs.core.config.interactor.entity.globalpermissions;

public class KlassTaxonomyPermissions implements IKlassTaxonomyPermissions {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected Boolean         canCreate;
  protected Boolean         canRead;
  protected Boolean         canEdit;
  protected Boolean         canDelete;
  protected String          entityId;
  protected String          type;
  protected Boolean         canDownload;
  
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
  public Boolean getCanCreate()
  {
    
    return canCreate;
  }
  
  @Override
  public void setCanCreate(Boolean canCreate)
  {
    this.canCreate = canCreate;
  }
  
  @Override
  public Boolean getCanRead()
  {
    
    return canRead;
  }
  
  @Override
  public void setCanRead(Boolean canRead)
  {
    this.canRead = canRead;
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
  public Boolean getCanDelete()
  {
    
    return canDelete;
  }
  
  @Override
  public void setCanDelete(Boolean canDelete)
  {
    this.canDelete = canDelete;
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
  public Boolean getCanDownload()
  {
    return canDownload;
  }
  
  @Override
  public void setCanDownload(Boolean canDownload)
  {
    this.canDownload = canDownload;
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
