package com.cs.core.config.interactor.entity.template;

public class PropertyCollectionPermission implements IPropertyCollectionPermission {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected Boolean         isVisible        = true;
  protected Boolean         isExpanded       = true;
  protected Boolean         canEdit          = true;
  protected String          entityId;
  protected String          propertyCollectionId;
  protected String          roleId;
  
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
  public Boolean getIsExpanded()
  {
    return isExpanded;
  }
  
  @Override
  public void setIsExpanded(Boolean isExpanded)
  {
    this.isExpanded = isExpanded;
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
  public String getPropertyCollectionId()
  {
    return propertyCollectionId;
  }
  
  @Override
  public void setPropertyCollectionId(String propertyCollectionId)
  {
    this.propertyCollectionId = propertyCollectionId;
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
