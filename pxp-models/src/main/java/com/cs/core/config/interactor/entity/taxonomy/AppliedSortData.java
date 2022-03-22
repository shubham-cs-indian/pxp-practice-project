package com.cs.core.config.interactor.entity.taxonomy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class AppliedSortData implements IAppliedSortData {
  
  private static final long serialVersionUID = 1L;
  
  protected List<String>    attributes;
  
  @Override
  public List<String> getAttributes()
  {
    if (attributes == null) {
      attributes = new ArrayList<>();
    }
    return attributes;
  }
  
  @Override
  public void setAttributes(List<String> attributes)
  {
    this.attributes = attributes;
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
  @JsonIgnore
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
  @JsonIgnore
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
  @JsonIgnore
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
