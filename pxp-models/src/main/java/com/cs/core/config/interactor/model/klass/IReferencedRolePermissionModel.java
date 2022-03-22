package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Set;

public interface IReferencedRolePermissionModel extends IModel {
  
  public static final String ENABLED_SECTION_ELEMENT_IDS      = "enabledSectionElementIds";
  public static final String VISIBLE_PROPERTY_COLLECTION_IDS  = "visiblePropertyCollectionIds";
  public static final String OPENED_PROPERTY_COLLECTION_IDS   = "openedPropertyCollectionIds";
  public static final String EDITABLE_PROPERTY_COLLECTION_IDS = "editablePropertyCollectionIds";
  public static final String EDITABLE_KLASS_AND_TAXONOMY_IDS  = "editableKlassAndTaxonomyIds";
  
  public Set<String> getVisiblePropertyCollectionIds();
  
  public void setVisiblePropertyCollectionIds(Set<String> visiblePropertyCollectionIds);
  
  public Set<String> getOpenedPropertyCollectionIds();
  
  public void setOpenedPropertyCollectionIds(Set<String> collapsedPropertyCollectionIds);
  
  public Set<String> getEnabledSectionElementIds();
  
  public void setEnabledSectionElementIds(Set<String> enabledSectionElementIds);
  
  public Set<String> getEditablePropertyCollectionIds();
  
  public void setEditablePropertyCollectionIds(Set<String> editablePropertyCollectionIds);
  
  public Set<String> getEditableKlassAndTaxonomyIds();
  
  public void setEditableKlassAndTaxonomyIds(Set<String> editableKlassAndTaxonomyIds);
}
