package com.cs.core.config.interactor.entity.tag;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class PropertyTag implements IPropertyTag {
  
  private static final long          serialVersionUID = 1L;
  
  protected String                   tagId;
  protected String                   label;
  protected List<IPropertyTagValues> tagValues;
  
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
  public List<IPropertyTagValues> getTagValues()
  {
    return tagValues;
  }
  
  @Override
  @JsonDeserialize(contentAs = PropertyTagValues.class)
  public void setTagValues(List<IPropertyTagValues> tagValues)
  {
    this.tagValues = tagValues;
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
}
