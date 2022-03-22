package com.cs.core.config.interactor.model.variantcontext;

import java.util.List;

public class VariantContextModifiedTagsModel implements IVariantContextModifiedTagsModel {
  
  protected String       tagId;
  protected List<String> addedTagValueIds;
  protected List<String> deletedTagValueIds;
  
  @Override
  public List<String> getAddedTagValueIds()
  {
    
    return addedTagValueIds;
  }
  
  @Override
  public void setAddedTagValueIds(List<String> addedTagValues)
  {
    this.addedTagValueIds = addedTagValues;
  }
  
  @Override
  public List<String> getDeletedTagValueIds()
  {
    
    return deletedTagValueIds;
  }
  
  @Override
  public void setDeletedTagValueIds(List<String> deletedTagValues)
  {
    this.deletedTagValueIds = deletedTagValues;
  }
  
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
}
