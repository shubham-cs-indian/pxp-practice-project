package com.cs.core.config.interactor.entity.attributiontaxonomy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class TagAndTagValuesIds implements ITagAndTagValuesIds {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected List<String>    tagValueIds;
  
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
  
  @Override
  @JsonIgnore
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
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
  @JsonIgnore
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
  @JsonIgnore
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
}
