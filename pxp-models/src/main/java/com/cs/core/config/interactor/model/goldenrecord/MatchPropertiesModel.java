package com.cs.core.config.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.entity.propertyinstance.AttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class MatchPropertiesModel implements IMatchPropertiesModel {
  
  private static final long          serialVersionUID = 1L;
  
  protected List<ITagInstance>       tags;
  protected List<IAttributeInstance> attributes;
  
  @Override
  public List<IAttributeInstance> getAttributes()
  {
    if (attributes == null) {
      attributes = new ArrayList<>();
    }
    return attributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = AttributeInstance.class)
  public void setAttributes(List<IAttributeInstance> attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public List<ITagInstance> getTags()
  {
    if (tags == null) {
      tags = new ArrayList<>();
    }
    return tags;
  }
  
  @Override
  @JsonDeserialize(contentAs = TagInstance.class)
  public void setTags(List<ITagInstance> tags)
  {
    this.tags = tags;
  }
}
