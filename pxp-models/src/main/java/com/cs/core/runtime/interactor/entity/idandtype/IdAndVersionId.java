package com.cs.core.runtime.interactor.entity.idandtype;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class IdAndVersionId implements IIdAndVersionId {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected Long            versionId;
  
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
