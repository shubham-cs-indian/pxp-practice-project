package com.cs.core.runtime.interactor.entity.klassinstance;

import com.cs.core.runtime.interactor.entity.configuration.base.AbstractRuntimeEntity;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRoleInstance;
import com.cs.core.runtime.interactor.entity.role.RoleInstance;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
@JsonTypeInfo(use = Id.NONE)
public abstract class AbstractKlassInstance extends AbstractRuntimeEntity
    implements IKlassInstance {
  
  private static final long                 serialVersionUID = 1L;
  
  protected String                          name;
  protected String                          baseType         = this.getClass()
      .getName();
  protected String                          componentId;
  protected List<IContentAttributeInstance> attributes;
  protected List<IContentTagInstance>       tags;
  protected List<IRoleInstance>             roles;
  protected List<String>                    types;
  protected List<String>                    taxonomyIds;
  protected List<String>                    selectedTaxonomyIds;
  protected String                          organizationId;
  protected String                          physicalCatalogId;
  protected String                          portalId;
  protected String                          logicalCatalogId;
  protected String                          systemId;
  protected String                          endpointId;
  protected String                          originalInstanceId;
  
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
  public List<? extends IContentAttributeInstance> getAttributes()
  {
    if (attributes == null) {
      attributes = new ArrayList<>();
    }
    return this.attributes;
  }
  
  @Override
  public void setAttributes(List<? extends IContentAttributeInstance> attributes)
  {
    this.attributes = (List<IContentAttributeInstance>) attributes;
  }
  
  @Override
  public List<IContentTagInstance> getTags()
  {
    if (tags == null) {
      tags = new ArrayList<>();
    }
    return tags;
  }
  
  @Override
  public void setTags(List<? extends IContentTagInstance> tags)
  {
    this.tags = (List<IContentTagInstance>) tags;
  }
  
  @Override
  public List<IRoleInstance> getRoles()
  {
    if (roles == null) {
      roles = new ArrayList<>();
    }
    return roles;
  }
  
  @JsonDeserialize(contentAs = RoleInstance.class)
  @Override
  public void setRoles(List<? extends IRoleInstance> roles)
  {
    this.roles = (List<IRoleInstance>) roles;
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
  public List<String> getTaxonomyIds()
  {
    if (taxonomyIds == null) {
      taxonomyIds = new ArrayList<>();
    }
    return taxonomyIds;
  }
  
  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    this.taxonomyIds = taxonomyIds;
  }
  
  @Override
  public List<String> getTypes()
  {
    if (types == null) {
      types = new ArrayList<>();
    }
    return types;
  }
  
  @Override
  public void setTypes(List<String> multiClassificationTypes)
  {
    this.types = multiClassificationTypes;
  }
  
  @Override
  public List<String> getSelectedTaxonomyIds()
  {
    if (selectedTaxonomyIds == null) {
      selectedTaxonomyIds = new ArrayList<>();
    }
    return selectedTaxonomyIds;
  }
  
  @Override
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds)
  {
    this.selectedTaxonomyIds = selectedTaxonomyIds;
  }
  
  @Override
  public String getComponentId()
  {
    return componentId;
  }
  
  @Override
  public void setComponentId(String componentId)
  {
    this.componentId = componentId;
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
  
  public String getPhysicalCatalogId()
  {
    return physicalCatalogId;
  }
  
  public void setPhysicalCatalogId(String physicalCatalogId)
  {
    this.physicalCatalogId = physicalCatalogId;
  }
  
  public String getPortalId()
  {
    return portalId;
  }
  
  public void setPortalId(String portalId)
  {
    this.portalId = portalId;
  }
  
  public String getLogicalCatalogId()
  {
    return logicalCatalogId;
  }
  
  public void setLogicalCatalogId(String logicalCatalogId)
  {
    this.logicalCatalogId = logicalCatalogId;
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
  public String getOriginalInstanceId()
  {
    return originalInstanceId;
  }
  
  @Override
  public void setOriginalInstanceId(String originalInstanceId)
  {
    this.originalInstanceId = originalInstanceId;
  }
}
