package com.cs.core.config.interactor.entity.globalpermissions;

public class GlobalPermission implements IGlobalPermission {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected Boolean         canRead          = false;
  protected Boolean         canEdit          = false;
  protected Boolean         canDelete        = false;
  protected Boolean         canCreate        = false;
  protected Boolean         canDownload      = false;
  protected String          type;
  protected String          code;
  protected String          roleId;
  protected String          entityId;
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
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
  public String getRoleId()
  {
    return roleId;
  }
  
  @Override
  public void setRoleId(String roleId)
  {
    this.roleId = roleId;
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
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
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
}
