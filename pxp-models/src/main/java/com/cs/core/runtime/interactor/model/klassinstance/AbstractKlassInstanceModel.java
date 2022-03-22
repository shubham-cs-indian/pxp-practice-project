package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRoleInstance;
import com.cs.core.runtime.interactor.entity.role.RoleInstance;
import com.cs.core.runtime.interactor.model.configdetails.IRuntimeModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonTypeInfo(use = Id.NONE)
public abstract class AbstractKlassInstanceModel implements IKlassInstance, IRuntimeModel {
  
  private static final long serialVersionUID = 1L;
  
  protected IKlassInstance  entity;
  protected String          originalInstanceId;
  protected Long            iid;
  
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
  public List<? extends IContentTagInstance> getTags()
  {
    return entity.getTags();
  }
  
  @Override
  public void setTags(List<? extends IContentTagInstance> tags)
  {
    entity.setTags(tags);
  }
  
  @Override
  public List<? extends IContentAttributeInstance> getAttributes()
  {
    return entity.getAttributes();
  }
  
  @Override
  public void setAttributes(List<? extends IContentAttributeInstance> attributes)
  {
    entity.setAttributes(attributes);
  }
  
  @Override
  public List<? extends IRoleInstance> getRoles()
  {
    return entity.getRoles();
  }
  
  @JsonDeserialize(contentAs = RoleInstance.class)
  @Override
  public void setRoles(List<? extends IRoleInstance> roles)
  {
    entity.setRoles(roles);
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
  public void setOwner(String owner)
  {
    entity.setOwner(owner);
  }*/
  
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
  public Long getVersionId()
  {
    return this.entity.getVersionId();
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.entity.setVersionId(versionId);
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
  public List<String> getTaxonomyIds()
  {
    return entity.getTaxonomyIds();
  }
  
  @Override
  public void setTaxonomyIds(List<String> taxonomyId)
  {
    entity.setTaxonomyIds(taxonomyId);
    ;
  }
  
  public List<String> getTypes()
  {
    return entity.getTypes();
  }
  
  @Override
  public void setTypes(List<String> multiClassificationTypes)
  {
    entity.setTypes(multiClassificationTypes);
  }
  
  public String getJobId()
  {
    return entity.getJobId();
  }
  
  public void setJobId(String jobId)
  {
    entity.setJobId(jobId);
  }
  
  @Override
  public List<String> getSelectedTaxonomyIds()
  {
    return entity.getSelectedTaxonomyIds();
  }
  
  @Override
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds)
  {
    entity.setSelectedTaxonomyIds(selectedTaxonomyIds);
  }
  
  @Override
  public String getComponentId()
  {
    return entity.getComponentId();
  }
  
  @Override
  public void setComponentId(String componentId)
  {
    this.entity.setComponentId(componentId);
  }
  
  @Override
  public String getPhysicalCatalogId()
  {
    return this.entity.getPhysicalCatalogId();
  }
  
  @Override
  public void setPhysicalCatalogId(String physicalCatelogId)
  {
    this.entity.setPhysicalCatalogId(physicalCatelogId);
  }
  
  @Override
  public String getPortalId()
  {
    return this.entity.getPortalId();
  }
  
  @Override
  public void setPortalId(String portalId)
  {
    this.entity.setPortalId(portalId);
  }
  
  @Override
  public String getLogicalCatalogId()
  {
    return this.entity.getLogicalCatalogId();
  }
  
  @Override
  public void setLogicalCatalogId(String logicalCatelogId)
  {
    this.entity.setLogicalCatalogId(logicalCatelogId);
  }
  
  @Override
  public String getSystemId()
  {
    return this.entity.getSystemId();
  }
  
  @Override
  public void setSystemId(String systemId)
  {
    this.entity.setSystemId(systemId);
  }
  
  @Override
  public String getOrganizationId()
  {
    return this.entity.getOrganizationId();
  }
  
  @Override
  public void setOrganizationId(String organizationId)
  {
    this.entity.setOrganizationId(organizationId);
  }
  
  @Override
  public String getEndpointId()
  {
    return this.entity.getEndpointId();
  }
  
  @Override
  public void setEndpointId(String endpointId)
  {
    this.entity.setEndpointId(endpointId);
  }
  
  @JsonIgnore
  public String getOriginalInstanceId()
  {
    return originalInstanceId;
  }
  
  @JsonIgnore
  public void setOriginalInstanceId(String originalInstanceId)
  {
    this.originalInstanceId = originalInstanceId;
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
