package com.cs.core.config.interactor.model.globalpermissions;

import java.util.Map;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.globalpermissions.IKlassTaxonomyPermissions;
import com.cs.core.config.interactor.entity.globalpermissions.IPropertyCollectionPermissions;
import com.cs.core.config.interactor.entity.globalpermissions.IPropertyPermissions;

public interface IGetGlobalPermissionsForRoleModel extends IConfigResponseWithAuditLogModel {
  
  public static final String ID                              = "id";
  public static final String TREE_PERMISSIONS                = "treePermissions";
  public static final String PROPERTY_COLLECTION_PERMISSIONS = "propertyCollectionPermissions";
  public static final String PROPERTY_PERMISSIONS            = "propertyPermissions";
  
  public String getId();
  
  public void setId(String id);
  
  public Map<String, IKlassTaxonomyPermissions> getTreePermissions();
  
  public void setTreePermissions(Map<String, IKlassTaxonomyPermissions> treePermissions);
  
  public Map<String, IPropertyCollectionPermissions> getPropertyCollectionPermissions();
  
  public void setPropertyCollectionPermissions(
      Map<String, IPropertyCollectionPermissions> propertyCollectionPermissions);
  
  public Map<String, IPropertyPermissions> getPropertyPermissions();
  
  public void setPropertyPermissions(Map<String, IPropertyPermissions> propertyPermissions);
}
