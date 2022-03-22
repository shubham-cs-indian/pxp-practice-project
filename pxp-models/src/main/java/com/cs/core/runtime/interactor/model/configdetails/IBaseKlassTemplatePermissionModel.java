package com.cs.core.runtime.interactor.model.configdetails;

import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Set;

public interface IBaseKlassTemplatePermissionModel extends IModel {
  
  public static final String GLOBAL_PERMISSION                  = "globalPermission";
  public static final String VISIBLE_PROPERTY_COLLECTION_IDS    = "visiblePropertyCollectionIds";
  public static final String EDITABLE_PROPERTY_COLLECTION_IDS   = "editablePropertyCollectionIds";
  public static final String EXPANDABLE_PROPERTY_COLLECTION_IDS = "expandablePropertyCollectionIds";
  public static final String VISIBLE_PROPERTY_IDS               = "visiblePropertyIds";
  public static final String EDITABLE_PROPERTY_IDS              = "editablePropertyIds";
  public static final String KLASS_IDS_HAVING_RP                = "klassIdsHavingRP";
  public static final String TAXONOMY_IDS_HAVING_RP             = "taxonomyIdsHavingRP";
  public static final String ALL_TAXONOMY_IDS_HAVING_RP         = "allTaxonomyIdsHavingRP";
  
  public IGlobalPermission getGlobalPermission();
  
  public void setGlobalPermission(IGlobalPermission globalPermission);
  
  public Set<String> getVisiblePropertyCollectionIds();
  
  public void setVisiblePropertyCollectionIds(Set<String> visiblePropertyCollectionIds);
  
  public Set<String> getEditablePropertyCollectionIds();
  
  public void setEditablePropertyCollectionIds(Set<String> editablePropertyCollectionIds);
  
  public Set<String> getExpandablePropertyCollectionIds();
  
  public void setExpandablePropertyCollectionIds(Set<String> expandablePropertyCollectionIds);
  
  public Set<String> getVisiblePropertyIds();
  
  public void setVisiblePropertyIds(Set<String> visiblePropertyIds);
  
  public Set<String> getEditablePropertyIds();
  
  public void setEditablePropertyIds(Set<String> editablePropertyIds);
  
  public Set<String> getKlassIdsHavingRP();
  
  public void setKlassIdsHavingRP(Set<String> klassIdsHavingReadPermission);
  
  public Set<String> getTaxonomyIdsHavingRP();
  
  public void setTaxonomyIdsHavingRP(Set<String> taxonomyIdsHavingRP);
  
  public Set<String> getAllTaxonomyIdsHavingRP();
  
  public void setAllTaxonomyIdsHavingRP(Set<String> allTaxonomyIdsHavingRP);
}
