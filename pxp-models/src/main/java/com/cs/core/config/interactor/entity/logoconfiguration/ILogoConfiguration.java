package com.cs.core.config.interactor.entity.logoconfiguration;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

public interface ILogoConfiguration extends IConfigEntity {
  
  public static final String PRIMARY_LOGO_ID   = "primaryLogoId";
  public static final String SECONDARY_LOGO_ID = "secondaryLogoId";
  public static final String FAVICON_ID        = "faviconId";
  public static final String TITLE             = "title";
  
  public String getPrimaryLogoId();
  
  public void setPrimaryLogoId(String primaryLogoId);
  
  public String getSecondaryLogoId();
  
  public void setSecondaryLogoId(String secondaryLogoId);
  
  public String getFaviconId();
  
  public void setFaviconId(String faviconId);
  
  public String getTitle();
  
  public void setTitle(String title);
}
