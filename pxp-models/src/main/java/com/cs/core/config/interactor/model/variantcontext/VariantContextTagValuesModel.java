package com.cs.core.config.interactor.model.variantcontext;

public class VariantContextTagValuesModel implements IVariantContextTagValuesModel {
  
  protected String  tagValueId;
  protected String  label;
  protected Boolean isSelected = false;
  protected String  color;
  
  @Override
  public String getTagValueId()
  {
    
    return tagValueId;
  }
  
  @Override
  public void setTagValueId(String tagValueId)
  {
    this.tagValueId = tagValueId;
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
  public Boolean getIsSelected()
  {
    
    return isSelected;
  }
  
  @Override
  public void setIsSelected(Boolean isSelected)
  {
    this.isSelected = isSelected;
  }
  
  @Override
  public String getColor()
  {
    
    return color;
  }
  
  @Override
  public void setColor(String color)
  {
    this.color = color;
  }
}
