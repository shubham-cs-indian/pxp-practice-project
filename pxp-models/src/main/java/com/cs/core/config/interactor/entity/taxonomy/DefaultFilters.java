package com.cs.core.config.interactor.entity.taxonomy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class DefaultFilters implements IDefaultFilters {
  
  private static final long       serialVersionUID = 1L;
  
  protected List<IFilterTagValue> tagValues;
  protected String                id;
  protected String                tagId;
  
  @Override
  public List<IFilterTagValue> getTagValues()
  {
    if (tagValues == null) {
      tagValues = new ArrayList<>();
    }
    return tagValues;
  }
  
  @Override
  @JsonDeserialize(contentAs = FilterTagValue.class)
  public void setTagValues(List<IFilterTagValue> tagValues)
  {
    this.tagValues = tagValues;
  }
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @JsonDeserialize
  public String getTagId()
  {
    return tagId;
  }
  
  @JsonDeserialize
  public void setTagId(String tagId)
  {
    this.tagId = tagId;
  }
  
  @JsonIgnore
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
  
  @JsonIgnore
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
  
  @JsonIgnore
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
