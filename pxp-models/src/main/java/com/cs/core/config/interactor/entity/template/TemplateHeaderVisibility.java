package com.cs.core.config.interactor.entity.template;

public class TemplateHeaderVisibility implements ITemplateHeaderVisibility {
  
  private static final long serialVersionUID      = 1L;
  protected Boolean         viewName              = true;
  protected Boolean         viewIcon              = true;
  protected Boolean         viewPrimaryType       = true;
  protected Boolean         viewAdditionalClasses = true;
  protected Boolean         viewTaxonomies        = true;
  protected Boolean         viewStatusTags        = true;
  protected Boolean         viewCreatedOn         = true;
  protected Boolean         viewLastModifiedBy    = true;
  protected String          id;
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
    this.viewLastModifiedBy = viewLastModifiedBy;
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
}
