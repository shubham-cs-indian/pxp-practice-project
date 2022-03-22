package com.cs.core.config.interactor.entity.variantcontext;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class VariantContextTagValues implements IVariantContextTagValues {
  
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
  
  @JsonIgnore
  @Override
  public String getId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setId(String id)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
}
