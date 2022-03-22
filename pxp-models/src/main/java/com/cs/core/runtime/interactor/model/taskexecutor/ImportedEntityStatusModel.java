package com.cs.core.runtime.interactor.model.taskexecutor;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.model.component.IImportedEntityStatus;
import com.cs.core.config.interactor.model.component.ImportedEntityStatus;

public class ImportedEntityStatusModel extends ImportedEntityStatus
    implements IImportedEntityStatusModel {
  
  private static final long serialVersionUID = 1L;
  
  IImportedEntityStatus     entity;
  
  public ImportedEntityStatusModel()
  {
    this.entity = new ImportedEntityStatus();
  }
  
  public ImportedEntityStatusModel(IImportedEntityStatus entity)
  {
    this.entity = entity;
  }
  
  @Override
  public IEntity getEntity()
  {
    return entity;
  }
  
  @Override
  public String getId()
  {
    return entity.getId();
  }
  
  @Override
  public void setId(String id)
  {
    entity.setId(id);
  }
  
  @Override
  public Long getVersionId()
  {
    return entity.getVersionId();
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    entity.setVersionId(versionId);
  }
  
  @Override
  public String getCreatedBy()
  {
    return entity.getCreatedBy();
  }
  
  @Override
  public void setCreatedBy(String createdBy)
  {
    entity.setCreatedBy(createdBy);
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return entity.getVersionTimestamp();
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    entity.setVersionTimestamp(versionTimestamp);
  }
  
  @Override
  public String getProcessInstanceId()
  {
    return entity.getProcessInstanceId();
  }
  
  @Override
  public void setProcessInstanceId(String processInstanceId)
  {
    entity.setProcessInstanceId(processInstanceId);
  }
  
  @Override
  public Long getCreatedOn()
  {
    return entity.getCreatedOn();
  }
  
  @Override
  public void setCreatedOn(Long createdOn)
  {
    entity.setCreatedOn(createdOn);
  }
  
  @Override
  public String getComponentId()
  {
    return entity.getComponentId();
  }
  
  /*@Override
  public String getOwner()
  {
    return entity.getOwner();
  }*/
  
  @Override
  public void setComponentId(String componentId)
  {
    entity.setComponentId(componentId);
  }
  
  /*@Override
  public void setOwner(String owner)
  {
    entity.setOwner(owner);
  }*/
  
  @Override
  public String getLastModifiedBy()
  {
    return entity.getLastModifiedBy();
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    entity.setLastModifiedBy(lastModifiedBy);
  }
  
  @Override
  public Long getLastModified()
  {
    return entity.getLastModified();
  }
  
  @Override
  public void setLastModified(Long lastModified)
  {
    entity.setLastModified(lastModified);
  }
  
  @Override
  public String getKlassInstanceId()
  {
    return entity.getKlassInstanceId();
  }
  
  @Override
  public void setKlassInstanceId(String klassInstanceId)
  {
    entity.setKlassInstanceId(klassInstanceId);
  }
  
  @Override
  public String getJobId()
  {
    return entity.getJobId();
  }
  
  @Override
  public void setJobId(String jobId)
  {
    entity.setJobId(jobId);
  }
  
  @Override
  public String getImportStatus()
  {
    return entity.getImportStatus();
  }
  
  @Override
  public void setImportStatus(String importStatus)
  {
    entity.setImportStatus(importStatus);
  }
  
  @Override
  public String getExceptionMessage()
  {
    return entity.getExceptionMessage();
  }
  
  @Override
  public void setExceptionMessage(String exceptionMessage)
  {
    entity.setExceptionMessage(exceptionMessage);
  }
}
