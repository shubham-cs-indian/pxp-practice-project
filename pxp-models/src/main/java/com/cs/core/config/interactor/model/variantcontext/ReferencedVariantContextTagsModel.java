package com.cs.core.config.interactor.model.variantcontext;

import java.util.List;

public class ReferencedVariantContextTagsModel implements IReferencedVariantContextTagsModel {
  
  protected String       tagId;
  protected List<String> tagValueIds;
  
  @Override
  public String getTagId()
  {
    
    return tagId;
  }
  
  @Override
  public void setTagId(String tagId)
  {
    this.tagId = tagId;
  }
  
  @Override
  public List<String> getTagValueIds()
  {
    
    return tagValueIds;
  }
  
  @Override
  public void setTagValueIds(List<String> tagValueIds)
  {
    this.tagValueIds = tagValueIds;
  }
}
