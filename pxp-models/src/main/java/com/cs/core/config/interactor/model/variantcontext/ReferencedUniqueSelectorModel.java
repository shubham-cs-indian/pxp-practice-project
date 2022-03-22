package com.cs.core.config.interactor.model.variantcontext;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class ReferencedUniqueSelectorModel implements IReferencedUniqueSelectorModel {
  
  private static final long                          serialVersionUID = 1L;
  
  protected List<IReferencedVariantContextTagsModel> tags;
  
  @Override
  public List<IReferencedVariantContextTagsModel> getTags()
  {
    return tags;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedVariantContextTagsModel.class)
  public void setTags(List<IReferencedVariantContextTagsModel> tags)
  {
    this.tags = tags;
  }
}
