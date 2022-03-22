package com.cs.core.config.interactor.entity.configuration.base;

import java.io.Serializable;

public interface IEntity extends Serializable {
  
  public static final String ID                = "id";
  public static final String VERSION_ID        = "versionId";
  public static final String VERSION_TIMESTAMP = "versionTimestamp";
  public static final String LAST_MODIFIED_BY  = "lastModifiedBy";
  
  public String getId();
  
  public void setId(String id);
  
  public Long getVersionId();
  
  public void setVersionId(Long versionId);
  
  public Long getVersionTimestamp();
  
  public void setVersionTimestamp(Long versionTimestamp);
  
  public String getLastModifiedBy();
  
  public void setLastModifiedBy(String lastModifiedBy);
  
  public String toString();
}
