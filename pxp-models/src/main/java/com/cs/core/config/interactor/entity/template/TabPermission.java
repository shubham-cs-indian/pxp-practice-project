package com.cs.core.config.interactor.entity.template;

public class TabPermission implements ITabPermission {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected String          type;
  protected Boolean         isVisible        = true;
  protected Boolean         canCreate        = true;
  protected Boolean         canEdit          = true;
  protected Boolean         canDelete        = true;
  protected String          entityId;
  protected String          code;
  
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
}
