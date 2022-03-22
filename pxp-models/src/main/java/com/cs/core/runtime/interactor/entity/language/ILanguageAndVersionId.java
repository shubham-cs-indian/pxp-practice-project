package com.cs.core.runtime.interactor.entity.language;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface ILanguageAndVersionId extends IEntity {
  
  public static final String LANGUAGE_CODE = "languageCode";
  public static final String VERSION_ID    = "versionId";
  
  public String getLanguageCode();
  
  public void setLanguageCode(String languageCode);
  
  public Long getVersionId();
  
  public void setVersionId(Long versionId);
}
