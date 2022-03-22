package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.runtime.interactor.entity.propertyinstance.IPropertyInstance;
import com.cs.core.runtime.interactor.model.datarule.IPropertyInstanceModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

public abstract class AbstractPropertyInstanceModel implements IPropertyInstanceModel {
  
  private static final long   serialVersionUID = 1L;
  
  protected IPropertyInstance entity;
  
  public AbstractPropertyInstanceModel(IPropertyInstance propertyInstance)
  {
    this.entity = propertyInstance;
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
  public Long getKlassInstanceVersion()
  {
    return entity.getKlassInstanceVersion();
  }
  
  @Override
  public void setKlassInstanceVersion(Long klassInstanceVersion)
  {
    entity.setKlassInstanceVersion(klassInstanceVersion);
  }
  
  @Override
  public String getBaseType()
  {
    return entity.getBaseType();
  }
  
  @Override
  public void setBaseType(String type)
  {
    entity.setBaseType(type);
  }
  
  @Override
  public IEntity getEntity()
  {
    return this.entity;
  }
  
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
  public Map<String, Object> getNotification()
  {
    return entity.getNotification();
  }
  
  @Override
  public void setNotification(Map<String, Object> notification)
  {
    entity.setNotification(notification);
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
  public String getVariantInstanceId()
  {
    return entity.getVariantInstanceId();
  }
  
  @Override
  public void setVariantInstanceId(String variantInstanceId)
  {
    entity.setVariantInstanceId(variantInstanceId);
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
  public Long getCreatedOn()
  {
    return entity.getCreatedOn();
  }
  
  @Override
  public void setCreatedOn(Long createdOn)
  {
    entity.setCreatedOn(createdOn);
  }
  
  /**
   * ******************* IGNORED FIELDS *********************
   */
  /*@Override
  @JsonIgnore
  public String getOwner()
  {
    // TODO Auto-generated method stub
    return null;
  }*/
  
  /*@Override
  @JsonIgnore
  public void setOwner(String owner)
  {
    // TODO Auto-generated method stub
  }*/
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
}
