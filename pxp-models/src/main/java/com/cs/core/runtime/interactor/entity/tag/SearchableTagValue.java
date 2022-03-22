package com.cs.core.runtime.interactor.entity.tag;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SearchableTagValue implements ISearchableTagValue {
  
  protected String  tagId;
  protected Integer relevance;
  protected Long    iid;
  
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
  public Integer getRelevance()
  {
    return relevance;
  }
  
  @Override
  public void setRelevance(Integer relevance)
  {
    this.relevance = relevance;
  }
  
  @JsonIgnore
  @Override
  public String getCreatedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setCreatedBy(String createdBy)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public Long getCreatedOn()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setCreatedOn(Long createdOn)
  {
    // TODO Auto-generated method stub
    
  }
  
  /*@JsonIgnore
  @Override
  public String getOwner()
  {
    // TODO Auto-generated method stub
    return null;
  }*/
  
  /*@JsonIgnore
  @Override
  public void setOwner(String owner)
  {
    // TODO Auto-generated method stub
  
  }*/
  
  @JsonIgnore
  @Override
  public Long getLastModified()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setLastModified(Long lastModified)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public String getJobId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setJobId(String jobId)
  {
    // TODO Auto-generated method stub
    
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
  public void setLastModifiedBy(String lastModifiedBy)
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
  public Long getIid()
  {
    // TODO Auto-generated method stub
    return iid;
  }
  
  @Override
  public void setIid(Long iid)
  {
    this.iid = iid;
  }
}
