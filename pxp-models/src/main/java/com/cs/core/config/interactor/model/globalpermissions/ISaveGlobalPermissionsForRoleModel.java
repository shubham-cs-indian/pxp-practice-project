package com.cs.core.config.interactor.model.globalpermissions;

import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermissionsForRole;
import com.cs.core.config.interactor.entity.globalpermissions.IKlassTaxonomyPermissions;
import com.cs.core.config.interactor.entity.globalpermissions.IPropertyCollectionPermissions;
import com.cs.core.config.interactor.entity.globalpermissions.IPropertyPermissions;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ISaveGlobalPermissionsForRoleModel extends IGlobalPermissionsForRole, IModel {
  
  public static final String ADDED_TREE_PERMISSIONS                   = "addedTreePermissions";
  public static final String MODIFIED_TREE_PERMISSIONS                = "modifiedTreePermissions";
  public static final String ADDED_PROPERTY_COLLECTION_PERMISSIONS    = "addedPropertyCollectionPermissions";
  public static final String MODIFIED_PROPERTY_COLLECTION_PERMISSIONS = "modifiedPropertyCollectionPermissions";
  public static final String ADDED_PROPERTY_PERMISSIONS               = "addedPropertyPermissions";
  public static final String MODIFIED_PROPERTY_PERMISSIONS            = "modifiedPropertyPermissions";
  public static final String SELECTED_KLASS_IDS                       = "selectedKlassIds";
  public static final String SELECTED_TAXONOMY_IDS                    = "selectedTaxonomyIds";
  public static final String SELECTED_PROPERTY_COLLECTION_ID          = "selectedPropertyCollectionId";
  
  public List<IKlassTaxonomyPermissions> getAddedTreePermissions();
  
  public void setAddedTreePermissions(List<IKlassTaxonomyPermissions> addedTreePermissions);
  
  public List<IKlassTaxonomyPermissions> getModifiedTreePermissions();
  
  public void setModifiedTreePermissions(List<IKlassTaxonomyPermissions> modifiedTreePermissions);
  
  public List<IPropertyCollectionPermissions> getAddedPropertyCollectionPermissions();
  
  public void setAddedPropertyCollectionPermissions(
      List<IPropertyCollectionPermissions> addedPropertyCollectionPermissions);
  
  public List<IPropertyCollectionPermissions> getModifiedPropertyCollectionPermissions();
  
  public void setModifiedPropertyCollectionPermissions(
      List<IPropertyCollectionPermissions> modifiedPropertyCollectionPermissions);
  
  public List<IPropertyPermissions> getAddedPropertyPermissions();
  
  public void setAddedPropertyPermissions(List<IPropertyPermissions> addedPropertyPermissions);
  
  public List<IPropertyPermissions> getModifiedPropertyPermissions();
  
  public void setModifiedPropertyPermissions(
      List<IPropertyPermissions> modifiedPropertyPermissions);
  
  public List<String> getSelectedKlassIds();
  
  public void setSelectedKlassIds(List<String> selectedKlassIds);
  
  public List<String> getSelectedTaxonomyIds();
  
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds);
  
  public String getSelectedPropertyCollectionId();
  
  public void setSelectedPropertyCollectionId(String selectedPropertyCollectionId);
}
