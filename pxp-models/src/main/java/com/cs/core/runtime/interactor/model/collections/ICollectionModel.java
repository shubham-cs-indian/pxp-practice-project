package com.cs.core.runtime.interactor.model.collections;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ICollectionModel extends IModel {
  
  public static final String ID         = "id";
  public static final String LABEL      = "label";
  public static final String PARENT_ID  = "parentId";
  public static final String TYPE       = "type";
  public static final String CREATED_BY = "createdBy";
  public static final String IS_PUBLIC  = "isPublic";
  public static final String CREATED_ON = "createdOn";
  
  public String getId();
  
  public void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getParentId();
  
  public void setParentId(String parentId);
  
  public String getType();
  
  public void setType(String type);
  
  Long getVersionTimestamp();
  
  void setVersionTimestamp(Long versionTimestamp);
  
  public String getCreatedBy();
  
  public void setCreatedBy(String createdBy);
  
  public Boolean getIsPublic();
  
  public void setIsPublic(Boolean isPublic);
  
  public Long getCreatedOn();
  
  public void setCreatedOn(Long createdOn);
}
