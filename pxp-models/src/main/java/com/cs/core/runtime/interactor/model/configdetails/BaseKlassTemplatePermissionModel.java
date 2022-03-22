package com.cs.core.runtime.interactor.model.configdetails;

import com.cs.core.config.interactor.entity.globalpermissions.GlobalPermission;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashSet;
import java.util.Set;

public class BaseKlassTemplatePermissionModel implements IBaseKlassTemplatePermissionModel {
  
  private static final long   serialVersionUID = 1L;
  protected Set<String>       visiblePropertyCollectionIds;
  protected Set<String>       editablePropertyCollectionIds;
  protected Set<String>       expandablePropertyCollectionIds;
  protected Set<String>       visiblePropertyIds;
  protected Set<String>       editablePropertyIds;
  protected IGlobalPermission globalPermission;
  protected Set<String>       klassIdsHavingReadPermission;
  protected Set<String>       taxonomyIdsHavingRP;
  protected Set<String>       allTaxonomyIdsHavingRP;
  
  public Set<String> getVisiblePropertyCollectionIds()
  {
    if (visiblePropertyCollectionIds == null) {
      return new HashSet<String>();
    }
    return visiblePropertyCollectionIds;
  }
  
  public void setVisiblePropertyCollectionIds(Set<String> visiblePropertyCollectionIds)
  {
    this.visiblePropertyCollectionIds = visiblePropertyCollectionIds;
  }
  
  public Set<String> getEditablePropertyCollectionIds()
  {
    if (editablePropertyCollectionIds == null) {
      return new HashSet<String>();
    }
    return editablePropertyCollectionIds;
  }
  
  @Override
  public void setEditablePropertyCollectionIds(Set<String> editablePropertyCollectionIds)
  {
    this.editablePropertyCollectionIds = editablePropertyCollectionIds;
  }
  
  @Override
  public Set<String> getExpandablePropertyCollectionIds()
  {
    if (expandablePropertyCollectionIds == null) {
      return new HashSet<String>();
    }
    return expandablePropertyCollectionIds;
  }
  
  @Override
  public void setExpandablePropertyCollectionIds(Set<String> expandablePropertyCollectionIds)
  {
    this.expandablePropertyCollectionIds = expandablePropertyCollectionIds;
  }
  
  @Override
  public Set<String> getVisiblePropertyIds()
  {
    if (visiblePropertyIds == null) {
      return new HashSet<String>();
    }
    return visiblePropertyIds;
  }
  
  @Override
  public void setVisiblePropertyIds(Set<String> visiblePropertyIds)
  {
    this.visiblePropertyIds = visiblePropertyIds;
  }
  
  @Override
  public Set<String> getEditablePropertyIds()
  {
    if (editablePropertyIds == null) {
      return new HashSet<String>();
    }
    return editablePropertyIds;
  }
  
  @Override
  public void setEditablePropertyIds(Set<String> editablePropertyIds)
  {
    this.editablePropertyIds = editablePropertyIds;
  }
  
  @Override
  public IGlobalPermission getGlobalPermission()
  {
    return globalPermission;
  }
  
  @JsonDeserialize(as = GlobalPermission.class)
  @Override
  public void setGlobalPermission(IGlobalPermission globalPermission)
  {
    this.globalPermission = globalPermission;
  }
  
  @Override
  public Set<String> getKlassIdsHavingRP()
  {
    return klassIdsHavingReadPermission;
  }
  
  @Override
  public void setKlassIdsHavingRP(Set<String> klassIdsHavingReadPermission)
  {
    this.klassIdsHavingReadPermission = klassIdsHavingReadPermission;
  }
  
  @Override
  public Set<String> getTaxonomyIdsHavingRP()
  {
    if (taxonomyIdsHavingRP == null) {
      taxonomyIdsHavingRP = new HashSet<>();
    }
    return taxonomyIdsHavingRP;
  }
  
  @Override
  public void setTaxonomyIdsHavingRP(Set<String> taxonomyIdsHavingRP)
  {
    this.taxonomyIdsHavingRP = taxonomyIdsHavingRP;
  }
  
  @Override
  public Set<String> getAllTaxonomyIdsHavingRP()
  {
    if (allTaxonomyIdsHavingRP == null) {
      allTaxonomyIdsHavingRP = new HashSet<>();
    }
    return allTaxonomyIdsHavingRP;
  }
  
  @Override
  public void setAllTaxonomyIdsHavingRP(Set<String> allTaxonomyIdsHavingRP)
  {
    this.allTaxonomyIdsHavingRP = allTaxonomyIdsHavingRP;
  }
}
