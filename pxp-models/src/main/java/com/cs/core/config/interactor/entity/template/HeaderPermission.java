package com.cs.core.config.interactor.entity.template;

public class HeaderPermission implements IHeaderPermission {
  
  private static final long serialVersionUID      = 1L;
  
  protected String          id;
  protected Boolean         viewName              = true;
  protected Boolean         canEditName           = true;
  protected Boolean         viewIcon              = true;
  protected Boolean         canChangeIcon         = true;
  protected Boolean         viewPrimaryType       = true;
  protected Boolean         canEditPrimaryType    = true;
  protected Boolean         viewAdditionalClasses = true;
  protected Boolean         canAddClasses         = true;
  protected Boolean         canDeleteClasses      = true;
  protected Boolean         viewTaxonomies        = true;
  protected Boolean         canAddTaxonomy        = true;
  protected Boolean         canDeleteTaxonomy     = true;
  protected Boolean         viewStatusTags        = true;
  protected Boolean         canEditStatusTag      = true;
  protected Boolean         viewCreatedOn         = true;
  protected Boolean         viewLastModifiedBy    = true;
  protected String          entityId;
  protected String          code;
  protected String          roleId;
  
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
  public Boolean getViewName()
  {
    return viewName;
  }
  
  @Override
  public void setViewName(Boolean viewName)
  {
    this.viewName = viewName;
  }
  
  @Override
  public Boolean getCanEditName()
  {
    return canEditName;
  }
  
  @Override
  public void setCanEditName(Boolean canEditName)
  {
    this.canEditName = canEditName;
  }
  
  @Override
  public Boolean getViewIcon()
  {
    return viewIcon;
  }
  
  @Override
  public void setViewIcon(Boolean viewIcon)
  {
    this.viewIcon = viewIcon;
  }
  
  @Override
  public Boolean getCanChangeIcon()
  {
    return canChangeIcon;
  }
  
  @Override
  public void setCanChangeIcon(Boolean canChangeIcon)
  {
    this.canChangeIcon = canChangeIcon;
  }
  
  @Override
  public Boolean getViewPrimaryType()
  {
    return viewPrimaryType;
  }
  
  @Override
  public void setViewPrimaryType(Boolean viewPrimaryType)
  {
    this.viewPrimaryType = viewPrimaryType;
  }
  
  @Override
  public Boolean getCanEditPrimaryType()
  {
    return canEditPrimaryType;
  }
  
  @Override
  public void setCanEditPrimaryType(Boolean canEditPrimaryType)
  {
    this.canEditPrimaryType = canEditPrimaryType;
  }
  
  @Override
  public Boolean getViewAdditionalClasses()
  {
    return viewAdditionalClasses;
  }
  
  @Override
  public void setViewAdditionalClasses(Boolean viewAdditionalClasses)
  {
    this.viewAdditionalClasses = viewAdditionalClasses;
  }
  
  @Override
  public Boolean getCanAddClasses()
  {
    return canAddClasses;
  }
  
  @Override
  public void setCanAddClasses(Boolean canAddClasses)
  {
    this.canAddClasses = canAddClasses;
  }
  
  @Override
  public Boolean getCanDeleteClasses()
  {
    return canDeleteClasses;
  }
  
  @Override
  public void setCanDeleteClasses(Boolean canDeleteClasses)
  {
    this.canDeleteClasses = canDeleteClasses;
  }
  
  @Override
  public Boolean getViewTaxonomies()
  {
    return viewTaxonomies;
  }
  
  @Override
  public void setViewTaxonomies(Boolean viewTaxonomies)
  {
    this.viewTaxonomies = viewTaxonomies;
  }
  
  @Override
  public Boolean getCanAddTaxonomy()
  {
    return canAddTaxonomy;
  }
  
  @Override
  public void setCanAddTaxonomy(Boolean canAddTaxonomy)
  {
    this.canAddTaxonomy = canAddTaxonomy;
  }
  
  @Override
  public Boolean getCanDeleteTaxonomy()
  {
    return canDeleteTaxonomy;
  }
  
  @Override
  public void setCanDeleteTaxonomy(Boolean canDeleteTaxonomy)
  {
    this.canDeleteTaxonomy = canDeleteTaxonomy;
  }
  
  @Override
  public Boolean getViewStatusTags()
  {
    return viewStatusTags;
  }
  
  @Override
  public void setViewStatusTags(Boolean viewStatusTags)
  {
    this.viewStatusTags = viewStatusTags;
  }
  
  @Override
  public Boolean getCanEditStatusTag()
  {
    return canEditStatusTag;
  }
  
  @Override
  public void setCanEditStatusTag(Boolean canEditStatusTag)
  {
    this.canEditStatusTag = canEditStatusTag;
  }
  
  @Override
  public Boolean getViewCreatedOn()
  {
    return viewCreatedOn;
  }
  
  @Override
  public void setViewCreatedOn(Boolean viewCreatedOn)
  {
    this.viewCreatedOn = viewCreatedOn;
  }
  
  @Override
  public Boolean getViewLastModifiedBy()
  {
    return viewLastModifiedBy;
  }
  
  @Override
  public void setViewLastModifiedBy(Boolean viewLastModifiedBy)
  {
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
