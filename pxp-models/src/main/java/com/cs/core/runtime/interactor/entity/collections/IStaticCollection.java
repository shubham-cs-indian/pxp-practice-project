package com.cs.core.runtime.interactor.entity.collections;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.runtime.interactor.entity.configuration.base.ISortEntity;
import java.util.List;

public interface IStaticCollection extends IEntity {
  
  public static final String ID                  = "id";
  public static final String LABEL               = "label";
  public static final String PARENT_ID           = "parentId";
  public static final String TYPE                = "type";
  public static final String KLASS_INSTANCE_IDS  = "klassInstanceIds";
  public static final String SORT_OPTIONS        = "sortOptions";
  public static final String IS_PUBLIC           = "isPublic";
  public static final String CREATED_BY          = "createdBy";
  public static final String CREATED_ON          = "createdOn";
  public static final String ORGANIZATION_ID     = "organizationId";
  public static final String ENDPOINT_ID         = "endpointId";
  public static final String PHYSICAL_CATELOG_ID = "physicalCatelogId";
  public static final String LOGICAL_CATELOG_ID  = "logicalCatelogId";
  public static final String SYSTEM_ID           = "systemId";
  public static final String PORTAL_ID           = "portalId";
  
  public String getId();
  
  public void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getParentId();
  
  public void setParentId(String parentId);
  
  public String getType();
  
  public void setType(String type);
  
  public Long getVersionTimestamp();
  
  public void setVersionTimestamp(Long versionTimestamp);
  
  public List<String> getKlassInstanceIds();
  
  public void setKlassInstanceIds(List<String> klassInstanceIds);
  
  public List<ISortEntity> getSortOptions();
  
  public void setSortOptions(List<ISortEntity> sortOptions);
  
  public Boolean getIsPublic();
  
  public void setIsPublic(Boolean isPublic);
  
  public String getCreatedBy();
  
  public void setCreatedBy(String createdBy);
  
  public Long getCreatedOn();
  
  public void setCreatedOn(Long createdOn);
  
  public String getOrganizationId();
  
  public void setOrganizationId(String organizationId);
  
  public String getSystemId();
  
  public void setSystemId(String systemId);
  
  public String getPhysicalCatelogId();
  
  public void setPhysicalCatelogId(String physicalCatelogId);
  
  public String getPortalId();
  
  public void setPortalId(String portalId);
  
  public String getLogicalCatelogId();
  
  public void setLogicalCatelogId(String logicalCatelogId);
  
  public String getEndpointId();
  
  public void setEndpointId(String endpointId);
}
