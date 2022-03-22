package com.cs.core.runtime.interactor.entity.datarule;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TaxonomyConflictingSource implements ITaxonomyConflictingSource{
  
  private static final long serialVersionUID = 1L;
  protected String          sourceContentId;
  protected String          sourceContentBaseType;
  protected String          relationshipId;
  protected String          relationshipSideId;
  
  @Override
  public String getSourceContentId()
  {
    return sourceContentId;
  }
  
  @Override
  public void setSourceContentId(String sourceContentId)
  {
    this.sourceContentId = sourceContentId;
  }
  
  @Override
  public String getSourceContentBaseType()
  {
    return sourceContentBaseType;
  }
  
  @Override
  public void setSourceContentBaseType(String sourceContentBaseType)
  {
    this.sourceContentBaseType = sourceContentBaseType;
  }
  
  @Override
  public String getRelationshipId()
  {
    return relationshipId;
  }
  
  @Override
  public void setRelationshipId(String relationshipId)
  {
    this.relationshipId = relationshipId;
  }
  
  @Override
  public String getRelationshipSideId()
  {
    return relationshipSideId;
  }
  
  @Override
  public void setRelationshipSideId(String relationshipSideId)
  {
    this.relationshipSideId = relationshipSideId;
  }

  @Override
  @JsonIgnore
  public String getCreatedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  @JsonIgnore
  public void setCreatedBy(String createdBy)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  @JsonIgnore
  public Long getCreatedOn()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  @JsonIgnore
  public void setCreatedOn(Long createdOn)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  @JsonIgnore
  public Long getLastModified()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  @JsonIgnore
  public void setLastModified(Long lastModified)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  @JsonIgnore
  public String getJobId()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  @JsonIgnore
  public void setJobId(String jobId)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  @JsonIgnore
  public String getId()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  @JsonIgnore
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
  public void setLastModifiedBy(String lastModifiedBy)
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
  public Long getIid()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setIid(Long iid)
  {
    // TODO Auto-generated method stub
    
  }
}
