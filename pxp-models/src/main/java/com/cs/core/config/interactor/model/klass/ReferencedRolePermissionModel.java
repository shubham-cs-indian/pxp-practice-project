package com.cs.core.config.interactor.model.klass;

import java.util.HashSet;
import java.util.Set;

public class ReferencedRolePermissionModel implements IReferencedRolePermissionModel {
  
  protected Set<String> enabledSectionElementIds;
  protected Set<String> visiblePropertyCollectionIds;
  protected Set<String> collapsedPropertyCollectionIds;
  protected Set<String> editablePropertyCollectionIds;
  protected Set<String> editableKlassAndTaxonomyIds;
  
  @Override
  public Set<String> getEnabledSectionElementIds()
  {
    if (enabledSectionElementIds == null) {
      enabledSectionElementIds = new HashSet<>();
    }
    return enabledSectionElementIds;
  }
  
  @Override
  public void setEnabledSectionElementIds(Set<String> enabledSectionElementIds)
  {
    this.enabledSectionElementIds = enabledSectionElementIds;
  }
  
  @Override
  public Set<String> getVisiblePropertyCollectionIds()
  {
    if (visiblePropertyCollectionIds == null) {
      visiblePropertyCollectionIds = new HashSet<>();
    }
    return visiblePropertyCollectionIds;
  }
  
  @Override
  public void setVisiblePropertyCollectionIds(Set<String> visiblePropertyCollectionIds)
  {
    this.visiblePropertyCollectionIds = visiblePropertyCollectionIds;
  }
  
  @Override
  public Set<String> getOpenedPropertyCollectionIds()
  {
    if (collapsedPropertyCollectionIds == null) {
      collapsedPropertyCollectionIds = new HashSet<>();
    }
    return collapsedPropertyCollectionIds;
  }
  
  @Override
  public void setOpenedPropertyCollectionIds(Set<String> collapsedPropertyCollectionIds)
  {
    this.collapsedPropertyCollectionIds = collapsedPropertyCollectionIds;
  }
  
  @Override
  public Set<String> getEditablePropertyCollectionIds()
  {
    if (editablePropertyCollectionIds == null) {
      editablePropertyCollectionIds = new HashSet<>();
    }
    
    return editablePropertyCollectionIds;
  }
  
  @Override
  public void setEditablePropertyCollectionIds(Set<String> editablePropertyCollectionIds)
  {
    this.editablePropertyCollectionIds = editablePropertyCollectionIds;
  }
  
  @Override
  public Set<String> getEditableKlassAndTaxonomyIds()
  {
    if (editableKlassAndTaxonomyIds == null) {
      editableKlassAndTaxonomyIds = new HashSet<>();
    }
    return editableKlassAndTaxonomyIds;
  }
  
  @Override
  public void setEditableKlassAndTaxonomyIds(Set<String> editableKlassAndTaxonomyIds)
  {
    this.editableKlassAndTaxonomyIds = editableKlassAndTaxonomyIds;
  }
}
