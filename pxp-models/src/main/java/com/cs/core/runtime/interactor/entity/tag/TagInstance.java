package com.cs.core.runtime.interactor.entity.tag;

import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.PropertyInstance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagInstance extends PropertyInstance implements ITagInstance {
  
  private static final long            serialVersionUID    = 1L;
  
  protected String                     tagId;
  protected List<ITagInstanceValue>    tagValues           = new ArrayList<>();
  protected String                     variantOf;
  protected Map<String, Object>        notification;
  protected String                     baseType            = this.getClass()
      .getName();
  protected List<ITagInstance>         tags                = new ArrayList<>();
  protected List<ITagConflictingValue> conflictingValues   = new ArrayList<>();
  protected Boolean                    isMatchAndMerge     = false;
  protected Boolean                    isMandatoryViolated = false;
  protected String                     contextInstanceId;
  protected Boolean                    isShouldViolated    = false;
  protected Boolean                    isConflictResolved  = false;
  
  @Override
  public List<ITagInstanceValue> getTagValues()
  {
    if (tagValues == null) {
      this.tagValues = new ArrayList<>();
    }
    return this.tagValues;
  }
  
  @JsonDeserialize(contentAs = TagInstanceValue.class)
  @Override
  public void setTagValues(List<ITagInstanceValue> tagValues)
  {
    this.tagValues = tagValues;
  }
  
  @Override
  public void setTagId(String tagId)
  {
    this.tagId = tagId;
  }
  
  @Override
  public String getTagId()
  {
    return this.tagId;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String type)
  {
    this.baseType = type;
  }
  
  public String getVariantOf()
  {
    return variantOf;
  }
  
  public void setVariantOf(String variantOf)
  {
    this.variantOf = variantOf;
  }
  
  @Override
  public List<? extends ITagInstance> getTags()
  {
    if (tags == null) {
      this.tags = new ArrayList<>();
    }
    return this.tags;
  }
  
  @SuppressWarnings("unchecked")
  @JsonDeserialize(contentAs = TagInstance.class)
  @Override
  public void setTags(List<? extends ITagInstance> tags)
  {
    this.tags = (List<ITagInstance>) tags;
  }
  
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    TagInstance other = (TagInstance) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    }
    else if (!id.equals(other.id))
      return false;
    return true;
  }
  
  @Override
  public Map<String, Object> getNotification()
  {
    if(notification == null) {
      notification = new HashMap<String, Object>();
    }
    return notification;
  }
  
  @Override
  public void setNotification(Map<String, Object> notification)
  {
    this.notification = notification;
  }
  
  @Override
  public List<ITagConflictingValue> getConflictingValues()
  {
    if (conflictingValues == null) {
      this.conflictingValues = new ArrayList<>();
    }
    return conflictingValues;
  }
  
  @JsonDeserialize(contentAs = TagConflictingValue.class)
  @Override
  public void setConflictingValues(List<ITagConflictingValue> conflictingValues)
  {
    this.conflictingValues = conflictingValues;
  }
  
  @Override
  public Boolean getIsMatchAndMerge()
  {
    return isMatchAndMerge;
  }
  
  @Override
  public void setIsMatchAndMerge(Boolean isMatchAndMerge)
  {
    this.isMatchAndMerge = isMatchAndMerge;
  }
  
  @Override
  public String getContextInstanceId()
  {
    return contextInstanceId;
  }
  
  @Override
  public void setContextInstanceId(String contextInstanceId)
  {
    this.contextInstanceId = contextInstanceId;
  }
  
  @Override
  public Boolean getIsMandatoryViolated()
  {
    return isMandatoryViolated;
  }
  
  @Override
  public void setIsMandatoryViolated(Boolean isMandatoryViolated)
  {
    this.isMandatoryViolated = isMandatoryViolated;
  }
  
  @Override
  public Boolean getIsShouldViolated()
  {
    return isShouldViolated;
  }
  
  @Override
  public void setIsShouldViolated(Boolean isShouldViolated)
  {
    this.isShouldViolated = isShouldViolated;
  }
  
  @Override
  public Boolean getIsConflictResolved()
  {
    if (isConflictResolved == null) {
      isConflictResolved = true;
    }
    return isConflictResolved;
  }
  
  @Override
  public void setIsConflictResolved(Boolean isConflictResolved)
  {
    this.isConflictResolved = isConflictResolved;
  }
}
