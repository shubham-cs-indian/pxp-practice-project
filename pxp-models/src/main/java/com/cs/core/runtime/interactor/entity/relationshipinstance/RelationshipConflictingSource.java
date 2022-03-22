package com.cs.core.runtime.interactor.entity.relationshipinstance;

import com.cs.core.runtime.interactor.entity.configuration.base.AbstractRuntimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class RelationshipConflictingSource extends AbstractRuntimeEntity
    implements IRelationshipConflictingSource {
  
  private static final long serialVersionUID = 1L;
  
  protected String          sourceContentId;
  protected String          sourceContentBaseType;
  protected String          relationshipId;
  protected String          relationshipSideId;
  protected String          couplingType;
  
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
  public String getCouplingType()
  {
    return couplingType;
  }
  
  @Override
  public void setCouplingType(String couplingType)
  {
    this.couplingType = couplingType;
  }
  
  /** *******************JSON Ignore******************* */
  @Override
  @JsonIgnore
  public String getId()
  {
    return id;
  }
  
  @Override
  @JsonIgnore
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  @JsonIgnore
  public Long getVersionId()
  {
    return versionId;
  }
  
  @Override
  @JsonIgnore
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  @JsonIgnore
  public Long getVersionTimestamp()
  {
    return versionTimestamp;
  }
  
  @Override
  @JsonIgnore
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.versionTimestamp = versionTimestamp;
  }
  
  @Override
  @JsonIgnore
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.lastModifiedBy = lastModifiedBy;
  }
  
  @Override
  @JsonIgnore
  public String getLastModifiedBy()
  {
    return lastModifiedBy;
  }
  
  @Override
  @JsonIgnore
  public String getCreatedBy()
  {
    return createdBy;
  }
  
  @Override
  @JsonIgnore
  public void setCreatedBy(String createdBy)
  {
    this.createdBy = createdBy;
  }
  
  @Override
  @JsonIgnore
  public Long getCreatedOn()
  {
    return createdOn;
  }
  
  @Override
  @JsonIgnore
  public void setCreatedOn(Long createdOn)
  {
    this.createdOn = createdOn;
  }
  
  @Override
  @JsonIgnore
  public Long getLastModified()
  {
    return lastModified;
  }
  
  @Override
  @JsonIgnore
  public void setLastModified(Long lastModified)
  {
    this.lastModified = lastModified;
  }
  
  @Override
  @JsonIgnore
  public String getJobId()
  {
    return jobId;
  }
  
  @Override
  @JsonIgnore
  public void setJobId(String jobId)
  {
    this.jobId = jobId;
  }
}
