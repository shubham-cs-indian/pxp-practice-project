package com.cs.core.config.interactor.entity.variantcontext;

import java.util.List;

public interface IAttributeContext extends IVariantContext {
  
  public static final String TAGS = "tags";
  
  public List<IVariantContextTag> getTags();
  
  public void setTags(List<IVariantContextTag> tags);
}
