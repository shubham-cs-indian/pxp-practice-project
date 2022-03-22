package com.cs.core.runtime.interactor.entity.configuration.base;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ReferenceInstance implements IReferenceInstance {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected String          referenceId;
  protected String          side1InstanceId;
  protected String          side2InstanceId;
  protected String          originalInstanceId;
  protected Long            iid;
  
  @Override
  public String getId()
  {
    return this.id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getSide1InstanceId()
  {
    return side1InstanceId;
  }
  
  @Override
  public void setSide1InstanceId(String side1Id)
  {
    this.side1InstanceId = side1Id;
  }
  
  @Override
  public String getSide2InstanceId()
  {
    return side2InstanceId;
  }
  
  @Override
  public void setSide2InstanceId(String side2Id)
  {
    this.side2InstanceId = side2Id;
  }
  
  @Override
  public String getReferenceId()
  {
    return referenceId;
  }
  
  @Override
  public void setReferenceId(String referenceId)
  {
    this.referenceId = referenceId;
  }
  
  @Override
  public String getOriginalInstanceId()
  {
    return originalInstanceId;
  }
  
  @Override
  public void setOriginalInstanceId(String originalInstanceId)
  {
    this.originalInstanceId = originalInstanceId;
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
    return iid;
  }
  
  @Override
  public void setIid(Long iid)
  {
    this.iid = iid;
  }
}
