package com.cs.core.runtime.interactor.entity.idandtype;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface IIdAndVersionId extends IEntity {
  
  public static final String ID         = "id";
  public static final String VERSION_ID = "versionId";
  
  public String getId();
  
  public void setId(String id);
  
  public Long getVersionId();
  
  public void setVersionId(Long versionId);
}
