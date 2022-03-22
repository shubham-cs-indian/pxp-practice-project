package com.cs.core.config.interactor.entity.tag;

public class PropertyTagValues implements IPropertyTagValues {
  
  private static final long serialVersionUID = 1L;
  
  protected String          tagValueId;
  protected String          label;
  protected String          color;
  protected Boolean         isSelected;
  
  @Override
  public String getTagValueId()
  {
    return this.tagValueId;
  }
  
  @Override
  public void setTagValueId(String tagValueId)
  {
    this.tagValueId = tagValueId;
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
  public String getId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setId(String id)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
}
