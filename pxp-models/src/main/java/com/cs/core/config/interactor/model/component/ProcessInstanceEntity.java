package com.cs.core.config.interactor.model.component;

public class ProcessInstanceEntity implements IProcessInstanceEntity {
  
  private static final long                     serialVersionUID = 1L;
  protected String                              id;
  protected Long                                versionId;
  protected Long                                versionTimeStamp;
  protected String                              lastModifiedBy;
  protected Long                                startTime;
  protected Long                                endTime;
  protected String                              label;
  protected String                              processId;
  protected String                              saveComment;
  protected String                              fileInstanceId;
  protected String                              endpointId;
  protected String                              systemId;
  protected String                              physicalCatalogId;
  protected String                              organizationId;
  protected Long                                userId;
  protected Integer                             status;
  protected Long                                processInstanceId;
  protected String                              taskInstanceId;
  protected String                              processDefinition;
  protected Long                                instanceIID;
  
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
  public Long getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return versionTimeStamp;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.versionTimeStamp = versionTimestamp;
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return lastModifiedBy;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.lastModifiedBy = lastModifiedBy;
  }
  
  @Override
  public Long getStartTime()
  {
    return startTime;
  }
  
  @Override
  public void setStartTime(Long startTime)
  {
    this.startTime = startTime;
  }
  
  @Override
  public Long getEndTime()
  {
    return endTime;
  }
  
  @Override
  public void setEndTime(Long endTime)
  {
    this.endTime = endTime;
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
  public String getProcessEventId()
  {
    return processId;
  }
  
  @Override
  public void setProcessEventId(String processId)
  {
    this.processId = processId;
  }
  
  @Override
  public String getSaveComment()
  {
    return saveComment;
  }
  
  @Override
  public void setSaveComment(String saveComment)
  {
    this.saveComment = saveComment;
  }
  
  @Override
  public String getFileInstanceId()
  {
    return fileInstanceId;
  }
  
  @Override
  public void setFileInstanceId(String fileInstanceId)
  {
    this.fileInstanceId = fileInstanceId;
  }
  
  @Override
  public String getSystemId()
  {
    return this.systemId;
  }
  
  @Override
  public void setSystemId(String systemId)
  {
    this.systemId = systemId;
  }
  
  @Override
  public String getEndpointId()
  {
    return this.endpointId;
  }
  
  @Override
  public void setEndpointId(String endpointId)
  {
    this.endpointId = endpointId;
  }
  
  @Override
  public String getPhysicalCatalogId()
  {
    return this.physicalCatalogId;
  }
  
  @Override
  public void setPhysicalCatalogId(String physicalCatalogId)
  {
    this.physicalCatalogId = physicalCatalogId;
  }
  
  @Override
  public String getOrganizationId()
  {
    return this.organizationId;
  }
  
  @Override
  public void setOrganizationId(String organizationId)
  {
    this.organizationId = organizationId;
  }
  
  @Override
  public Long getUserId()
  {
    return this.userId;
  }
  
  @Override
  public void setUserId(Long userId)
  {
    this.userId = userId;
  }
  
  @Override
  public Integer getStatus()
  {
    return status;
  }

  @Override
  public void setStatus(Integer status)
  {
    this.status = status;
  }
  
  @Override
  public Long getProcessInstanceId()
  {
    return processInstanceId;
  }

  @Override
  public void setProcessInstanceId(Long processInstanceId)
  {
    this.processInstanceId = processInstanceId;
  }
  // PXPFDEV-15368 : added for getting task details from workflow
  /** STARTS **/  
  @Override
  public String getProcessDefinition()
  {
    return processDefinition;
  }

  @Override
  public void setProcessDefinition(String processDefinition)
  {
    this.processDefinition = processDefinition;
    
  }

  @Override
  public Long getInstanceIID()
  {
    return instanceIID;
  }

  @Override
  public void setInstanceIID(Long instanceIID)
  {
    this.instanceIID = instanceIID;
  }
  
  /**ENDS **/
}
