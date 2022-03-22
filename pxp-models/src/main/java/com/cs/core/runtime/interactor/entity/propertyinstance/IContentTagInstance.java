package com.cs.core.runtime.interactor.entity.propertyinstance;

import com.cs.core.runtime.interactor.entity.tag.ITagConflictingValue;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
    property = "baseType", visible = true)
public interface IContentTagInstance extends IPropertyInstance {
  
  public static final String TAG_VALUES         = "tagValues";
  public static final String TAG_ID             = "tagId";
  public static final String TAGS               = "tags";
  public static final String CONFLICTING_VALUES = "conflictingValues";
  
  public List<ITagConflictingValue> getConflictingValues();
  
  public void setConflictingValues(List<ITagConflictingValue> conflictingValues);
  
  public List<ITagInstanceValue> getTagValues();
  
  public void setTagValues(List<ITagInstanceValue> tagValues);
  
  public void setTagId(String tagId);
  
  public String getTagId();
  
  public List<? extends ITagInstance> getTags();
  
  public void setTags(List<? extends ITagInstance> tags);
}
