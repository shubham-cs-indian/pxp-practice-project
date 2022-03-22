package com.cs.core.config.interactor.entity.globalpermissions;

import java.util.List;

public class GlobalPermissionsForRole implements IGlobalPermissionsForRole {
  
  private static final long                      serialVersionUID = 1L;
  
  protected String                               id;
  protected List<IKlassTaxonomyPermissions>      treePermissions;
  protected List<IPropertyCollectionPermissions> propertyCollectionPermissions;
  protected List<IPropertyPermissions>           propertyPermissions;
  
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
  public List<IKlassTaxonomyPermissions> getTreePermissions()
  {
    
    return treePermissions;
  }
  
  @Override
  public void setTreePermissions(List<IKlassTaxonomyPermissions> treePermissions)
  {
    this.treePermissions = treePermissions;
  }
  
  @Override
  public List<IPropertyCollectionPermissions> getPropertyCollectionPermissions()
  {
    
    return propertyCollectionPermissions;
  }
  
  @Override
  public void setPropertyCollectionPermissions(
      List<IPropertyCollectionPermissions> propertyCollectionPermissions)
  {
    this.propertyCollectionPermissions = propertyCollectionPermissions;
  }
  
  @Override
  public List<IPropertyPermissions> getPropertyPermissions()
  {
    
    return propertyPermissions;
  }
  
  @Override
  public void setPropertyPermissions(List<IPropertyPermissions> propertyPermissions)
  {
    this.propertyPermissions = propertyPermissions;
  }
  
  @Override
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
}
