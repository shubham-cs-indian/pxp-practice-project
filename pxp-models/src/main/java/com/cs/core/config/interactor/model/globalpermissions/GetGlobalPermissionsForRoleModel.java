package com.cs.core.config.interactor.model.globalpermissions;

import java.util.Map;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.globalpermissions.IKlassTaxonomyPermissions;
import com.cs.core.config.interactor.entity.globalpermissions.IPropertyCollectionPermissions;
import com.cs.core.config.interactor.entity.globalpermissions.IPropertyPermissions;
import com.cs.core.config.interactor.entity.globalpermissions.KlassTaxonomyPermissions;
import com.cs.core.config.interactor.entity.globalpermissions.PropertyCollectionPermissions;
import com.cs.core.config.interactor.entity.globalpermissions.PropertyPermissions;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetGlobalPermissionsForRoleModel extends ConfigResponseWithAuditLogModel
    implements IGetGlobalPermissionsForRoleModel {
  
  protected String                                      id;
  protected Map<String, IKlassTaxonomyPermissions>      treePermissions;
  protected Map<String, IPropertyCollectionPermissions> propertyCollectionPermissions;
  protected Map<String, IPropertyPermissions>           propertyPermissions;
  
  @Override
  public Map<String, IKlassTaxonomyPermissions> getTreePermissions()
  {
    
    return treePermissions;
  }
  
  @Override
  @JsonDeserialize(contentAs = KlassTaxonomyPermissions.class)
  public void setTreePermissions(Map<String, IKlassTaxonomyPermissions> treePermissions)
  {
    this.treePermissions = treePermissions;
  }
  
  @Override
  public Map<String, IPropertyCollectionPermissions> getPropertyCollectionPermissions()
  {
    
    return propertyCollectionPermissions;
  }
  
  @Override
  @JsonDeserialize(contentAs = PropertyCollectionPermissions.class)
  public void setPropertyCollectionPermissions(
      Map<String, IPropertyCollectionPermissions> propertyCollectionPermissions)
  {
    this.propertyCollectionPermissions = propertyCollectionPermissions;
  }
  
  @Override
  public Map<String, IPropertyPermissions> getPropertyPermissions()
  {
    
    return propertyPermissions;
  }
  
  @Override
  @JsonDeserialize(contentAs = PropertyPermissions.class)
  public void setPropertyPermissions(Map<String, IPropertyPermissions> propertyPermissions)
  {
    this.propertyPermissions = propertyPermissions;
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
}
