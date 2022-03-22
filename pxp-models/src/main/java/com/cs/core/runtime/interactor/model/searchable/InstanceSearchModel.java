package com.cs.core.runtime.interactor.model.searchable;

import com.cs.core.config.interactor.model.role.IRoleModel;
import com.cs.core.runtime.interactor.model.filter.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class InstanceSearchModel implements IInstanceSearchModel {
  
  private static final long                              serialVersionUID = 1L;
  protected Integer                                      from;
  protected Integer                                      size;
  protected List<IRoleModel>                             roles;
  protected String                                       id;
  protected List<? extends IPropertyInstanceFilterModel> attributes;
  protected List<? extends IPropertyInstanceFilterModel> tags;
  protected String                                       allSearch;
  protected List<? extends IFilterValueModel>            selectedRoles;
  protected List<String>                                 selectedTypes;
  protected Boolean                                      isRed;
  protected Boolean                                      isYellow;
  protected Boolean                                      isOrange;
  protected Boolean                                      isGreen;
  protected List<String>                                 selectedTaxonomyIds;
  protected String                                       parentTaxonomyId;
  protected List<ISortModel>                             sortOptions;
  protected String                                       moduleId;
  protected List<String>                                 xRayAttributes;
  protected List<String>                                 xRayTags;
  protected String                                       organizationId;
  protected String                                       physicalCatalogId;
  protected String                                       logicalCatalogId;
  protected String                                       systemId;
  protected String                                       endpointId;
  protected Integer                                      expiryStatus     = 0;
  
  @Override
  public Boolean getIsGreen()
  {
    return isGreen;
  }
  
  @Override
  public void setIsGreen(Boolean isGreen)
  {
    this.isGreen = isGreen;
  }
  
  @Override
  public Boolean getIsRed()
  {
    return isRed;
  }
  
  @Override
  public void setIsRed(Boolean isRed)
  {
    this.isRed = isRed;
  }
  
  @Override
  public Boolean getIsOrange()
  {
    return isOrange;
  }
  
  @Override
  public void setIsOrange(Boolean isOrange)
  {
    this.isOrange = isOrange;
  }
  
  @Override
  public Boolean getIsYellow()
  {
    return isYellow;
  }
  
  @Override
  public void setIsYellow(Boolean isYellow)
  {
    this.isYellow = isYellow;
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
  public List<IRoleModel> getRoles()
  {
    if (roles == null) {
      roles = new ArrayList<IRoleModel>();
    }
    return roles;
  }
  
  @Override
  public void setRoles(List<IRoleModel> roles)
  {
    this.roles = roles;
  }
  
  @Override
  public Integer getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(Integer from)
  {
    this.from = from;
  }
  
  @Override
  public Integer getSize()
  {
    return size;
  }
  
  @Override
  public void setSize(Integer size)
  {
    this.size = size;
  }
  
  @Override
  public List<? extends IPropertyInstanceFilterModel> getAttributes()
  {
    if (attributes == null) {
      attributes = new ArrayList<>();
    }
    return attributes;
  }
  
  @JsonDeserialize(contentAs = PropertyInstanceValueTypeFilterModer.class)
  @Override
  public void setAttributes(List<? extends IPropertyInstanceFilterModel> attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public List<? extends IPropertyInstanceFilterModel> getTags()
  {
    if (tags == null) {
      tags = new ArrayList<>();
    }
    return tags;
  }
  
  @JsonDeserialize(contentAs = PropertyInstanceRangeTypeFilterModel.class)
  @Override
  public void setTags(List<? extends IPropertyInstanceFilterModel> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public String getAllSearch()
  {
    return allSearch;
  }
  
  @Override
  public void setAllSearch(String allSearch)
  {
    this.allSearch = allSearch;
  }
  
  @Override
  public List<? extends IFilterValueModel> getSelectedRoles()
  {
    if (selectedRoles == null) {
      selectedRoles = new ArrayList<>();
    }
    return selectedRoles;
  }
  
  @Override
  public void setSelectedRoles(List<? extends IFilterValueModel> selectedRoles)
  {
    this.selectedRoles = selectedRoles;
  }
  
  public List<String> getSelectedTypes()
  {
    if (selectedTypes == null) {
      selectedTypes = new ArrayList<>();
    }
    return selectedTypes;
  }
  
  @Override
  public void setSelectedTypes(List<String> selectedTypes)
  {
    this.selectedTypes = selectedTypes;
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
  public String getParentTaxonomyId()
  {
    return parentTaxonomyId;
  }
  
  @Override
  public void setParentTaxonomyId(String parentTaxonomyId)
  {
    this.parentTaxonomyId = parentTaxonomyId;
  }
  
  @Override
  public String getModuleId()
  {
    return moduleId;
  }
  
  @Override
  public void setModuleId(String moduleId)
  {
    this.moduleId = moduleId;
  }
  
  @Override
  public List<String> getXRayAttributes()
  {
    if (xRayAttributes == null) {
      xRayAttributes = new ArrayList<String>();
    }
    return xRayAttributes;
  }
  
  @Override
  public void setXRayAttributes(List<String> xRayAttributes)
  {
    this.xRayAttributes = xRayAttributes;
  }
  
  @Override
  public List<String> getXRayTags()
  {
    if (xRayTags == null) {
      xRayTags = new ArrayList<String>();
    }
    return xRayTags;
  }
  
  @Override
  public void setXRayTags(List<String> xRayTags)
  {
    this.xRayTags = xRayTags;
  }
  
  @Override
  public String getOrganizationId()
  {
    return organizationId;
  }
  
  @Override
  public void setOrganizationId(String organizationId)
  {
    this.organizationId = organizationId;
  }
  
  @Override
  public String getSystemId()
  {
    return systemId;
  }
  
  @Override
  public void setSystemId(String systemId)
  {
    this.systemId = systemId;
  }
  
  @Override
  public String getPhysicalCatalogId()
  {
    return physicalCatalogId;
  }
  
  @Override
  public void setPhysicalCatalogId(String physicalCatalogId)
  {
    this.physicalCatalogId = physicalCatalogId;
  }
  
  @Override
  public String getLogicalCatalogId()
  {
    return logicalCatalogId;
  }
  
  @Override
  public void setLogicalCatalogId(String logicalCatalogId)
  {
    this.logicalCatalogId = logicalCatalogId;
  }
  
  @Override
  public String getEndpointId()
  {
    return endpointId;
  }
  
  @Override
  public void setEndpointId(String endpointId)
  {
    this.endpointId = endpointId;
  }
  
  @Override
  public List<ISortModel> getSortOptions()
  {
    if (sortOptions == null) {
      sortOptions = new ArrayList<>();
    }
    return sortOptions;
  }
  
  @JsonDeserialize(contentAs = SortModel.class)
  @Override
  public void setSortOptions(List<ISortModel> sortOptions)
  {
    this.sortOptions = sortOptions;
  }
  
  @Override
  public Integer getExpiryStatus()
  {
    return expiryStatus;
  }
  
  @Override
  public void setExpiryStatus(Integer expiryStatus)
  {
    this.expiryStatus = expiryStatus;
  }
}
