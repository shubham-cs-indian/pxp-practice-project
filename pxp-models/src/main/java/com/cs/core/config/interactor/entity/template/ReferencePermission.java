package com.cs.core.config.interactor.entity.template;

public class ReferencePermission implements IReferencePermission {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected Boolean         isVisible        = true;
  protected Boolean         canAdd           = true;
  protected Boolean         canDelete        = true;
  protected String          entityId;
  protected String          code;
  protected String          roleId;
  protected String          referenceId;
  
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
  public Boolean getIsVisible()
  {
    return isVisible;
  }
  
  @Override
  public void setIsVisible(Boolean isVisible)
  {
    this.isVisible = isVisible;
  }
  
  @Override
  public Boolean getCanAdd()
  {
    return canAdd;
  }
  
  @Override
  public void setCanAdd(Boolean canAdd)
  {
    this.canAdd = canAdd;
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
  public Long getVersionId()
  {
    return null;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return null;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return null;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
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
  public String getReferenceId()
  {
    return referenceId;
  }
  
  @Override
  public void setReferenceId(String referenceId)
  {
    this.referenceId = referenceId;
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
}
