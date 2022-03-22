package com.cs.core.config.interactor.model.variantcontext;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class VariantContextTagModel implements IVariantContextTagModel {
  
  private static final long                     serialVersionUID = 1L;
  protected String                              tagId;
  protected String                              label;
  protected List<IVariantContextTagValuesModel> tagValues;
  
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
  public String getLabel()
  {
    
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public List<IVariantContextTagValuesModel> getTagValues()
  {
    
    return tagValues;
  }
  
  @Override
  @JsonDeserialize(contentAs = VariantContextTagValuesModel.class)
  public void setTagValues(List<IVariantContextTagValuesModel> tagValues)
  {
    this.tagValues = tagValues;
  }
}
