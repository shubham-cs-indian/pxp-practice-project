package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.entity.klassinstance.IKlassStatisticsInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.KlassStatisticsInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IStatistics;
import com.cs.core.runtime.interactor.entity.propertyinstance.Statistics;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class KlassStatisticsInstanceModel implements IKlassStatisticsInstanceModel {
  
  private static final long          serialVersionUID = 1L;
  protected IKlassStatisticsInstance entity;
  protected Long                     iid;
  
  public KlassStatisticsInstanceModel()
  {
    entity = new KlassStatisticsInstance();
  }
  
  @Override
  public String getName()
  {
    return entity.getName();
  }
  
  @Override
  public void setName(String name)
  {
    entity.setName(name);
  }
  
  @Override
  public String getBaseType()
  {
    return entity.getBaseType();
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    entity.setBaseType(baseType);
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
  public String getParentVariantInstanceId()
  {
    return entity.getParentVariantInstanceId();
  }
  
  @Override
  public void setParentVariantInstanceId(String parentVariantId)
  {
    entity.setParentVariantInstanceId(parentVariantId);
  }
  
  @Override
  public List<IStatistics> getDataGovernance()
  {
    return entity.getDataGovernance();
  }
  
  @JsonDeserialize(contentAs = Statistics.class)
  @Override
  public void setDataGovernance(List<IStatistics> dataGovernance)
  {
    entity.setDataGovernance(dataGovernance);
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
  
  /*@Override
  public String getOwner()
  {
    return entity.getOwner();
  }*/
  
  /*@Override
    public void setOwner(String owner)
    {
      entity.setOwner(owner);
    }
  */
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
