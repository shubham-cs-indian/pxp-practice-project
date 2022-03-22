package com.cs.core.config.interactor.model.articlemarketrelationship;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IContentDetailsModel extends IModel {
  
  public static final String ID         = "id";
  public static final String VERSION_ID = "versionId";
  public static final String COUNT      = "count";
  
  public String getId();
  
  public void setId(String id);
  
  public Long getVersionId();
  
  public void setVersionId(Long versionId);
  
  public Integer getCount();
  
  public void setCount(Integer count);
}
