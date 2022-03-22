package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.entity.datarule.AttributeConflictingValue;
import com.cs.core.runtime.interactor.entity.datarule.IAttributeConflictingValue;
import com.cs.core.runtime.interactor.entity.tag.ITagConflictingValue;
import com.cs.core.runtime.interactor.entity.tag.TagConflictingValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

public class ContextualDataForPropagationModel implements IContextualDataForPropagationModel {
  
  private static final long                         serialVersionUID = 1L;
  protected String                                  sourceContentId;
  protected String                                  contextId;
  protected Map<String, IAttributeConflictingValue> attributes;
  protected Map<String, ITagConflictingValue>       tags;
  
  @Override
  public String getSourceContentId()
  {
    return sourceContentId;
  }
  
  @Override
  public void setSourceContentId(String sourceContentId)
  {
    this.sourceContentId = sourceContentId;
  }
  
  @Override
  public String getContextId()
  {
    return contextId;
  }
  
  @Override
  public void setContextId(String contextId)
  {
    this.contextId = contextId;
  }
  
  @Override
  public Map<String, IAttributeConflictingValue> getAttributes()
  {
    if (attributes == null) {
      attributes = new HashMap<>();
    }
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
    if (tags == null) {
      tags = new HashMap<>();
    }
    return tags;
  }
  
  @JsonDeserialize(contentAs = TagConflictingValue.class)
  @Override
  public void setTags(Map<String, ITagConflictingValue> tags)
  {
    this.tags = tags;
  }
}
