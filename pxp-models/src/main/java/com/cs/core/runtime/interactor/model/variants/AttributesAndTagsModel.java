package com.cs.core.runtime.interactor.model.variants;

import java.util.ArrayList;
import java.util.List;

public class AttributesAndTagsModel implements IAttributesAndTagsModel {
  
  private static final long serialVersionUID = 1L;
  
  protected List<String>    tagIds;
  protected List<String>    attributeIds;
  
  @Override
  public List<String> getTagIds()
  {
    if (tagIds == null) {
      tagIds = new ArrayList<String>();
    }
    return tagIds;
  }
  
  @Override
  public void setTagIds(List<String> tagIds)
  {
    this.tagIds = tagIds;
  }
  
  @Override
  public List<String> getAttributeIds()
  {
    return attributeIds;
  }
  
  @Override
  public void setAttributeIds(List<String> attributeIds)
  {
    this.attributeIds = attributeIds;
  }
}
