package com.cs.core.runtime.interactor.entity.klassinstance;

import com.cs.core.runtime.interactor.entity.configuration.base.AbstractRuntimeEntity;
import com.cs.core.runtime.interactor.entity.propertyinstance.IStatistics;
import com.cs.core.runtime.interactor.entity.propertyinstance.Statistics;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

public class KlassStatisticsInstance extends AbstractRuntimeEntity
    implements IKlassStatisticsInstance {
  
  private static final long   serialVersionUID = 1L;
  protected String            name;
  protected String            baseType;
  protected String            klassInstanceId;
  protected String            parentVariantId;
  protected List<IStatistics> dataGovernance;
  
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
  public String getName()
  {
    return name;
  }
  
  @Override
  public void setName(String name)
  {
    this.name = name;
  }
  
  /*@Override
  public String getOwner()
  {
    return owner;
  }*/
  
  /*@Override
  public void setOwner(String owner)
  {
    this.owner = owner;
  }*/
  
  @Override
  public String getCreatedBy()
  {
    return createdBy;
  }
  
  @Override
  public void setCreatedBy(String createdBy)
  {
    this.createdBy = createdBy;
  }
  
  @Override
  public Long getCreatedOn()
  {
    return createdOn;
  }
  
  @Override
  public void setCreatedOn(Long createdOn)
  {
    this.createdOn = createdOn;
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
    return versionTimestamp;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.versionTimestamp = versionTimestamp;
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
  public Long getLastModified()
  {
    return lastModified;
  }
  
  @Override
  public void setLastModified(Long lastModified)
  {
    this.lastModified = lastModified;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
  
  @Override
  public String getKlassInstanceId()
  {
    return klassInstanceId;
  }
  
  @Override
  public void setKlassInstanceId(String masterContentId)
  {
    this.klassInstanceId = masterContentId;
  }
  
  @Override
  public String getParentVariantInstanceId()
  {
    return parentVariantId;
  }
  
  @Override
  public void setParentVariantInstanceId(String parentVariantId)
  {
    this.parentVariantId = parentVariantId;
  }
  
  @Override
  public List<IStatistics> getDataGovernance()
  {
    return dataGovernance;
  }
  
  @JsonDeserialize(contentAs = Statistics.class)
  @Override
  public void setDataGovernance(List<IStatistics> dataGovernance)
  {
    this.dataGovernance = dataGovernance;
  }
}
