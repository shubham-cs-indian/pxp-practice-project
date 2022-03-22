package com.cs.core.config.interactor.entity.variantcontext;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class AttributeContext extends VariantContext implements IAttributeContext {
  
  private static final long          serialVersionUID = 1L;
  protected List<IVariantContextTag> tags;
  
  @Override
  public List<IVariantContextTag> getTags()
  {
    return tags;
  }
  
  @Override
  @JsonDeserialize(contentAs = VariantContextTag.class)
  public void setTags(List<IVariantContextTag> tags)
  {
    this.tags = tags;
  }
}
