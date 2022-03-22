package com.cs.core.runtime.interactor.entity.configuration.base;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface IRuntimeEntity extends IEntity {
  
  public static final String JOB_ID        = "jobId";
  public static final String LAST_MODIFIED = "lastModified";
  public static final String CREATED_BY    = "createdBy";
  public static final String CREATED_ON    = "createdOn";
  // public static final String OWNER = "owner";
  public static final String IID           = "iid";
  
  public String getCreatedBy();
  
  public void setCreatedBy(String createdBy);
  
  public Long getCreatedOn();
  
  public void setCreatedOn(Long createdOn);
  
  /*public String getOwner();
  public void setOwner(String owner);
  */
  public Long getLastModified();
  
  public void setLastModified(Long lastModified);
  
  public String getJobId();
  
  public void setJobId(String jobId);
  
  public Long getIid();
  
  public void setIid(Long iid);
}
