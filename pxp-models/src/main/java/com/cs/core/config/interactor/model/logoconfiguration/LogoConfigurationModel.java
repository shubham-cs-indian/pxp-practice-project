package com.cs.core.config.interactor.model.logoconfiguration;

import com.cs.core.config.interactor.entity.logoconfiguration.ILogoConfiguration;
import com.cs.core.config.interactor.entity.logoconfiguration.LogoConfiguration;

public class LogoConfigurationModel implements ILogoConfigurationModel {
  
  private static final long serialVersionUID = 1L;
  ILogoConfiguration        entity;
  
  public LogoConfigurationModel()
  {
    this.entity = new LogoConfiguration();
  }
  
  public LogoConfigurationModel(ILogoConfiguration entity)
  {
    super();
    this.entity = entity;
  }
  
  @Override
  public ILogoConfiguration getEntity()
  {
    return entity;
  }
  
  @Override
  public String getPrimaryLogoId()
  {
    return entity.getPrimaryLogoId();
  }
  
  @Override
  public void setPrimaryLogoId(String primaryLogoId)
  {
    entity.setPrimaryLogoId(primaryLogoId);
  }
  
  @Override
  public String getSecondaryLogoId()
  {
    return entity.getSecondaryLogoId();
  }
  
  @Override
  public void setSecondaryLogoId(String secondaryLogoId)
  {
    entity.setSecondaryLogoId(secondaryLogoId);
  }
  
  @Override
  public String getFaviconId()
  {
    return entity.getFaviconId();
  }
  
  @Override
  public void setFaviconId(String faviconId)
  {
    entity.setFaviconId(faviconId);
  }
  
  @Override
  public String getTitle()
  {
    return entity.getTitle();
  }
  
  @Override
  public void setTitle(String title)
  {
    entity.setTitle(title);
  }
  
  @Override
  public String getCode()
  {
    return entity.getCode();
  }
  
  @Override
  public void setCode(String code)
  {
    entity.setCode(code);
  }
  
  @Override
  public String getId()
  {
    return entity.getId();
  }
  
  @Override
  public void setId(String id)
  {
    entity.setId(id);
  }
  
  @Override
  public Long getVersionId()
  {
    return entity.getVersionId();
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    entity.setVersionId(versionId);
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return entity.getVersionTimestamp();
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    entity.setVersionTimestamp(versionTimestamp);
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return entity.getLastModifiedBy();
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    entity.setLastModifiedBy(lastModifiedBy);
  }
}
