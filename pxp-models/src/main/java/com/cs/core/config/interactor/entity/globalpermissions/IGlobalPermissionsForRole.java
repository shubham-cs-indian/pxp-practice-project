package com.cs.core.config.interactor.entity.globalpermissions;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

import java.util.List;

public interface IGlobalPermissionsForRole extends IEntity {
  
  public static final String TREE_PERMISSIONS                = "treePermissions";
  public static final String PROPERTY_COLLECTION_PERMISSIONS = "propertyCollectionPermissions";
  public static final String PROPERTY_PERMISSIONS            = "propertyPermissions";
  
  public List<IKlassTaxonomyPermissions> getTreePermissions();
  
  public void setTreePermissions(List<IKlassTaxonomyPermissions> treePermissions);
  
  public List<IPropertyCollectionPermissions> getPropertyCollectionPermissions();
  
  public void setPropertyCollectionPermissions(
      List<IPropertyCollectionPermissions> propertyCollectionPermissions);
  
  public List<IPropertyPermissions> getPropertyPermissions();
  
  public void setPropertyPermissions(List<IPropertyPermissions> propertyPermissions);
}
