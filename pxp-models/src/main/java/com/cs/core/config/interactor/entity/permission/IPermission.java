package com.cs.core.config.interactor.entity.permission;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.template.*;

import java.util.Map;

public interface IPermission extends IConfigEntity {
  
  public static final String LABEL                          = "label";
  public static final String GLOBAL_PERMISSION              = "globalPermission";
  public static final String HEADER_PERMISSION              = "headerPermission";
  public static final String PROPERTY_COLLECTION_PERMISSION = "propertyCollectionPermission";
  public static final String PROPERTY_PERMISSION            = "propertyPermission";
  public static final String RELATIONSHIP_PERMISSION        = "relationshipPermission";
  public static final String REFERENCE_PERMISSIONS          = "referencePermissions";
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public IGlobalPermission getGlobalPermission();
  
  public void setGlobalPermission(IGlobalPermission globalPermission);
  
  public IHeaderPermission getHeaderPermission();
  
  public void setHeaderPermission(IHeaderPermission headerPermission);
  
  public Map<String, IPropertyCollectionPermission> getPropertyCollectionPermission();
  
  public void setPropertyCollectionPermission(
      Map<String, IPropertyCollectionPermission> propertyCollectionPermission);
  
  public Map<String, IPropertyPermission> getPropertyPermission();
  
  public void setPropertyPermission(Map<String, IPropertyPermission> propertyPermission);
  
  public Map<String, IRelationshipPermission> getRelationshipPermission();
  
  public void setRelationshipPermission(
      Map<String, IRelationshipPermission> relationshipPermission);
  
  public Map<String, IReferencePermission> getReferencePermissions();
  
  public void setReferencePermissions(Map<String, IReferencePermission> referencePermissions);
}
