package com.cs.core.runtime.interactor.entity.language;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class LanguageAndVersionId implements ILanguageAndVersionId {
  
  private static final long serialVersionUID = 1L;
  
  protected String          languageCode;
  protected Long            versionId;
  
  @Override
  public String getLanguageCode()
  {
    return languageCode;
  }
  
  @Override
  public void setLanguageCode(String languageCode)
  {
    this.languageCode = languageCode;
  }
  
  @Override
  public Long getVersionId()
  {
    return this.versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  /** ************* ignored fields ***************** */
  @Override
  @JsonIgnore
  public String getId()
  {
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setId(String id)
  {
  }
  
  @Override
  @JsonIgnore
  public Long getVersionTimestamp()
  {
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setVersionTimestamp(Long versionTimestamp)
  {
  }
  
  @Override
  @JsonIgnore
  public void setLastModifiedBy(String lastModifiedBy)
  {
  }
  
  @Override
  @JsonIgnore
  public String getLastModifiedBy()
  {
    return null;
  }
}
