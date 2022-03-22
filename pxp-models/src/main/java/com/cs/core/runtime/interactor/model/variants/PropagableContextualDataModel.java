package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.runtime.interactor.entity.datarule.AttributeConflictingValue;
import com.cs.core.runtime.interactor.entity.datarule.IAttributeConflictingValue;
import com.cs.core.runtime.interactor.entity.tag.ITagConflictingValue;
import com.cs.core.runtime.interactor.entity.tag.TagConflictingValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class PropagableContextualDataModel implements IPropagableContextualDataModel {
  
  private static final long                         serialVersionUID = 1L;
  protected Map<String, IAttributeConflictingValue> attributes;
  protected Map<String, ITagConflictingValue>       tags;
  
  @Override
  public Map<String, IAttributeConflictingValue> getAttributes()
  {
    return attributes;
  }
  
  @JsonDeserialize(contentAs = AttributeConflictingValue.class)
  @Override
  public void setAttributes(Map<String, IAttributeConflictingValue> attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public Map<String, ITagConflictingValue> getTags()
  {
    return tags;
  }
  
  @JsonDeserialize(contentAs = TagConflictingValue.class)
  @Override
  public void setTags(Map<String, ITagConflictingValue> tags)
  {
    this.tags = tags;
  }
}
