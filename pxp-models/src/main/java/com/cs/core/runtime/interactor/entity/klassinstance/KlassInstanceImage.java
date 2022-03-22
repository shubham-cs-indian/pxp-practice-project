package com.cs.core.runtime.interactor.entity.klassinstance;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class KlassInstanceImage implements IKlassInstanceImage {
  
  private static final long serialVersionUID = 1L;
  
  protected String          assetObjectKey;
  
  protected String          type;
  
  @Override
  public String getAssetObjectKey()
  {
    return this.assetObjectKey;
  }
  
  @Override
  public void setAssetObjectKey(String imageOriginalKey)
  {
    this.assetObjectKey = imageOriginalKey;
  }
  
  @Override
  public String getType()
  {
    return this.type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @JsonIgnore
  @Override
  public Long getVersionId()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setVersionId(Long versionId)
  {
  }
  
  @JsonIgnore
  @Override
  public Long getVersionTimestamp()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
  }
  
  @JsonIgnore
  @Override
  public String getLastModifiedBy()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
  }
  
  @JsonIgnore
  @Override
  public String getId()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setId(String id)
  {
  }
}
