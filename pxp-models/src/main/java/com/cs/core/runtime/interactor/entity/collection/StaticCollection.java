package com.cs.core.runtime.interactor.entity.collection;

import com.cs.core.runtime.interactor.entity.collections.IStaticCollection;
import com.cs.core.runtime.interactor.entity.configuration.base.ISortEntity;
import com.cs.core.runtime.interactor.entity.configuration.base.SortEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.List;

public class StaticCollection implements IStaticCollection {
  
  private static final long   serialVersionUID = 1L;
  
  protected String            id;
  protected String            label;
  protected String            parentId;
  protected String            type;
  protected Long              versionTimestamp;
  protected List<String>      klassInstanceIds;
  protected List<ISortEntity> sortOptions;
  protected Boolean           isPublic         = true;
  protected String            createdBy;
  protected Long              createdOn;
  protected String            organizationId;
  protected String            physicalCatelogId;
  protected String            portalId;
  protected String            logicalCatelogId;
  protected String            systemId;
  protected String            endpointId;
  
  @Override
  public List<String> getKlassInstanceIds()
  {
    if (klassInstanceIds == null) {
      klassInstanceIds = new ArrayList<>();
    }
    return klassInstanceIds;
  }
  
  @Override
  public void setKlassInstanceIds(List<String> klassInstanceIds)
  {
    this.klassInstanceIds = klassInstanceIds;
  }
  
  @Override
  public List<ISortEntity> getSortOptions()
  {
    if (sortOptions == null) {
      sortOptions = new ArrayList<>();
    }
    return sortOptions;
  }
  
  @JsonDeserialize(contentAs = SortEntity.class)
  @Override
  public void setSortOptions(List<ISortEntity> sortOptions)
  {
    this.sortOptions = sortOptions;
  }
  
  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
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
  public String getParentId()
  {
    return parentId;
  }
  
  @Override
  public void setParentId(String parentId)
  {
    this.parentId = parentId;
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
  public Boolean getIsPublic()
  {
    return isPublic;
  }
  
  @Override
  public void setIsPublic(Boolean isPublic)
  {
    this.isPublic = isPublic;
  }
  
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
  
  public String getOrganizationId()
  {
    return organizationId;
  }
  
  public void setOrganizationId(String organizationId)
  {
    this.organizationId = organizationId;
  }
  
  public String getSystemId()
  {
    return systemId;
  }
  
  public void setSystemId(String systemId)
  {
    this.systemId = systemId;
  }
  
  public String getPhysicalCatelogId()
  {
    return physicalCatelogId;
  }
  
  public void setPhysicalCatelogId(String physicalCatelogId)
  {
    this.physicalCatelogId = physicalCatelogId;
  }
  
  @Override
  public String getPortalId()
  {
    return portalId;
  }
  
  @Override
  public void setPortalId(String portalId)
  {
    this.portalId = portalId;
  }
  
  public String getLogicalCatelogId()
  {
    return logicalCatelogId;
  }
  
  public void setLogicalCatelogId(String logicalCatelogId)
  {
    this.logicalCatelogId = logicalCatelogId;
  }
  
  public String getEndpointId()
  {
    return endpointId;
  }
  
  public void setEndpointId(String endpointId)
  {
    this.endpointId = endpointId;
  }
  
  @Override
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
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
}
