package com.cs.core.config.interactor.model.xray;

import com.cs.core.config.interactor.entity.attribute.AbstractAttribute;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.runtime.interactor.model.configdetails.IXRayConfigDetailsModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class XRayConfigDetailsModel implements IXRayConfigDetailsModel {
  
  private static final long         serialVersionUID = 1L;
  
  protected Map<String, IAttribute> referencedAttributes;
  protected Map<String, ITag>       referencedTags;
  
  @Override
  public Map<String, IAttribute> getReferencedAttributes()
  {
    return referencedAttributes;
  }
  
  @JsonDeserialize(contentAs = AbstractAttribute.class)
  @Override
  public void setReferencedAttributes(Map<String, IAttribute> referencedAttributes)
  {
    this.referencedAttributes = referencedAttributes;
  }
  
  @Override
  public Map<String, ITag> getReferencedTags()
  {
    return referencedTags;
  }
  
  @JsonDeserialize(contentAs = Tag.class)
  @Override
  public void setReferencedTags(Map<String, ITag> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
}
