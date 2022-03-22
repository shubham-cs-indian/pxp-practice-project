package com.cs.core.config.interactor.model.mapping;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ITagValueMappingModel extends IModel {
  
  public static final String ID                  = "id";
  public static final String TAG_VALUE           = "tagValue";
  public static final String MAPPED_TAG_VALUE_ID = "mappedTagValueId";
  public static final String IS_IGNORE_CASE      = "isIgnoreCase";
  public static final String IS_AUTOMAPPED       = "isAutomapped";
  
  public String getId();
  
  public void setId(String id);
  
  public String getTagValue();
  
  public void setTagValue(String tagValue);
  
  public String getMappedTagValueId();
  
  public void setMappedTagValueId(String mappedTagValueId);
  
  public Boolean getIsIgnoreCase();
  
  public void setIsIgnoreCase(Boolean isIgnoreCase);
  
  public Boolean getIsAutomapped();
  
  public void setIsAutomapped(Boolean isAutomapped);
}
