package com.cs.core.runtime.interactor.model.collections;

import com.cs.core.runtime.interactor.entity.collection.StaticCollection;
import com.cs.core.runtime.interactor.entity.collections.IStaticCollection;
import com.cs.core.runtime.interactor.entity.configuration.base.ISortEntity;
import com.cs.core.runtime.interactor.entity.configuration.base.SortEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class StaticCollectionModel implements IStaticCollectionModel {
  
  private static final long   serialVersionUID = 1L;
  
  protected IStaticCollection entity;
  
  public StaticCollectionModel()
  {
    entity = new StaticCollection();
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
  public String getLabel()
  {
    return entity.getLabel();
  }
  
  @Override
  public void setLabel(String label)
  {
    entity.setLabel(label);
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
  public String getParentId()
  {
    return entity.getParentId();
  }
  
  @Override
  public void setParentId(String parentId)
  {
    entity.setParentId(parentId);
  }
  
  @Override
  public String toString()
  {
    return entity.toString();
  }
  
  @Override
  public String getType()
  {
    return entity.getType();
  }
  
  @Override
  public void setType(String type)
  {
    entity.setType(type);
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
  public List<String> getKlassInstanceIds()
  {
    return entity.getKlassInstanceIds();
  }
  
  @Override
  public void setKlassInstanceIds(List<String> klassInstanceIds)
  {
    entity.setKlassInstanceIds(klassInstanceIds);
  }
  
  @Override
  public List<ISortEntity> getSortOptions()
  {
    return entity.getSortOptions();
  }
  
  @Override
  @JsonDeserialize(contentAs = SortEntity.class)
  public void setSortOptions(List<ISortEntity> sortOptions)
  {
    entity.setSortOptions(sortOptions);
  }
  
  @Override
  public Boolean getIsPublic()
  {
    return entity.getIsPublic();
  }
  
  @Override
  public void setIsPublic(Boolean isPublic)
  {
    entity.setIsPublic(isPublic);
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
  
  @Override
  public String getOrganizationId()
  {
    return entity.getOrganizationId();
  }
  
  @Override
  public void setOrganizationId(String organizationId)
  {
    entity.setOrganizationId(organizationId);
  }
  
  @Override
  public String getSystemId()
  {
    return entity.getSystemId();
  }
  
  @Override
  public void setSystemId(String systemId)
  {
    entity.setSystemId(systemId);
  }
  
  @Override
  public String getPhysicalCatelogId()
  {
    return entity.getPhysicalCatelogId();
  }
  
  @Override
  public void setPhysicalCatelogId(String physicalCatelogId)
  {
    entity.setPhysicalCatelogId(physicalCatelogId);
  }
  
  @Override
  public String getPortalId()
  {
    return entity.getPortalId();
  }
  
  @Override
  public void setPortalId(String portalId)
  {
    entity.setPortalId(portalId);
  }
  
  @Override
  public String getLogicalCatelogId()
  {
    return entity.getLogicalCatelogId();
  }
  
  @Override
  public void setLogicalCatelogId(String logicalCatelogId)
  {
    entity.setLogicalCatelogId(logicalCatelogId);
  }
  
  @Override
  public String getEndpointId()
  {
    return entity.getEndpointId();
  }
  
  @Override
  public void setEndpointId(String endpointId)
  {
    entity.setEndpointId(endpointId);
  }
}
