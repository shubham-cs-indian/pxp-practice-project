package com.cs.core.config.interactor.entity.klass;

public abstract class AbstractKlassStructurePermissionSetting
    implements IKlassStructurePermissionSetting {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  
  protected Boolean         isHeaderEditable;
  
  protected Boolean         isHeaderVisible;
  
  protected Boolean         canAddSiblings;
  
  protected Boolean         canAddChildren;
  
  protected Boolean         canMove;
  
  protected Boolean         canDelete;
  
  protected Boolean         isVariantAllowed;
  
  protected Boolean         shouldVersion;
  
  protected Long            versionId;
  
  protected Long            versionTimestamp;
  
  protected String          lastModifiedBy;
  
  protected String          code;
  
  @Override
  public String getCode()
  {
    return this.code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
  }
  
  @Override
  public String getId()
  {
    return this.id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public Boolean getIsHeaderEditable()
  {
    return this.isHeaderEditable;
  }
  
  @Override
  public void setIsHeaderEditable(Boolean isHeaderEditable)
  {
    this.isHeaderEditable = isHeaderEditable;
  }
  
  @Override
  public Boolean getIsHeaderVisible()
  {
    return this.isHeaderVisible;
  }
  
  @Override
  public void setIsHeaderVisible(Boolean isHeaderVisible)
  {
    this.isHeaderVisible = isHeaderVisible;
  }
  
  @Override
  public Boolean getCanAddSiblings()
  {
    return this.canAddSiblings;
  }
  
  @Override
  public void setCanAddSiblings(Boolean canAddSiblings)
  {
    this.canAddSiblings = canAddSiblings;
  }
  
  @Override
  public Boolean getCanAddChildren()
  {
    return this.canAddChildren;
  }
  
  @Override
  public void setCanAddChildren(Boolean canAddChildren)
  {
    this.canAddChildren = canAddChildren;
  }
  
  @Override
  public Boolean getCanMove()
  {
    return this.canMove;
  }
  
  @Override
  public void setCanMove(Boolean canMove)
  {
    this.canMove = canMove;
  }
  
  @Override
  public Boolean getCanDelete()
  {
    return this.canDelete;
  }
  
  @Override
  public void setCanDelete(Boolean canDelete)
  {
    this.canDelete = canDelete;
  }
  
  @Override
  public Boolean getIsVariantAllowed()
  {
    return this.isVariantAllowed;
  }
  
  @Override
  public void setIsVariantAllowed(Boolean isVariantAllowed)
  {
    this.isVariantAllowed = isVariantAllowed;
  }
  
  @Override
  public Boolean getShouldVersion()
  {
    return this.shouldVersion;
  }
  
  @Override
  public void setShouldVersion(Boolean shouldVersion)
  {
    this.shouldVersion = shouldVersion;
  }
  
  @Override
  public Long getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return versionTimestamp;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.versionTimestamp = versionTimestamp;
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return lastModifiedBy;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.lastModifiedBy = lastModifiedBy;
  }
}
