package com.cs.core.config.interactor.entity.logoconfiguration;

public class LogoConfiguration implements ILogoConfiguration {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected Long            versionId;
  protected Long            versionTimestamp;
  protected String          lastModifiedBy;
  protected String          code;
  protected String          faviconId;
  protected String          primaryLogoId;
  protected String          secondaryLogoId;
  protected String          title;
  
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
  public String getFaviconId()
  {
    return faviconId;
  }
  
  @Override
  public void setFaviconId(String faviconId)
  {
    this.faviconId = faviconId;
  }
  
  @Override
  public String getPrimaryLogoId()
  {
    return primaryLogoId;
  }
  
  @Override
  public void setPrimaryLogoId(String primaryLogoId)
  {
    this.primaryLogoId = primaryLogoId;
  }
  
  @Override
  public String getSecondaryLogoId()
  {
    return secondaryLogoId;
  }
  
  @Override
  public void setSecondaryLogoId(String secondaryLogoId)
  {
    this.secondaryLogoId = secondaryLogoId;
  }
  
  @Override
  public String getTitle()
  {
    return title;
  }
  
  @Override
  public void setTitle(String title)
  {
    this.title = title;
  }
}
