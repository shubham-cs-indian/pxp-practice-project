package com.cs.core.config.interactor.model.permission;

import java.util.List;
import java.util.Map;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.permission.IPermission;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;

public interface IGetPermissionModel extends IConfigResponseWithAuditLogModel {
  
  public static final String PERMISSION                      = "permission";
  public static final String REFERENCED_PROPERTY_COLLECTIONS = "referencedPropertyCollections";
  public static final String REFERENCED_RELATIONSHIPS        = "referencedRelationships";
  public static final String ALLOWED_TEMPLATES               = "allowedTemplates";
  
  public IPermission getPermission();
  
  public void setPermission(IPermission permission);
  
  public Map<String, IReferencedPropertyCollectionForPermissionModel> getReferencedPropertyCollections();
  
  public void setReferencedPropertyCollections(
      Map<String, IReferencedPropertyCollectionForPermissionModel> referencedPropertyCollections);
  
  public Map<String, IConfigEntityInformationModel> getReferencedRelationships();
  
  public void setReferencedRelationships(
      Map<String, IConfigEntityInformationModel> referencedRelationships);
  
  public List<IIdLabelCodeModel> getAllowedTemplates();
  
  public void setAllowedTemplates(List<IIdLabelCodeModel> allowedTemplates);
  
}
