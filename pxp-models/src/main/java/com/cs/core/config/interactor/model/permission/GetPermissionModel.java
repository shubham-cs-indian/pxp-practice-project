package com.cs.core.config.interactor.model.permission;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.permission.IPermission;
import com.cs.core.config.interactor.entity.permission.Permission;
import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetPermissionModel extends ConfigResponseWithAuditLogModel implements IGetPermissionModel {
  
  private static final long                                              serialVersionUID = 1L;
  protected IPermission                                                  permission;
  protected Map<String, IReferencedPropertyCollectionForPermissionModel> referencedPropertyCollections;
  protected Map<String, IConfigEntityInformationModel>                   referencedRelationships;
  protected Map<String, IConfigEntityInformationModel>                   referencedReferences;
  protected List<IIdLabelCodeModel>                                      allowedTemplates;
  
  public IPermission getPermission()
  {
    return permission;
  }
  
  @JsonDeserialize(as = Permission.class)
  public void setPermission(IPermission permission)
  {
    this.permission = permission;
  }
  
  public Map<String, IReferencedPropertyCollectionForPermissionModel> getReferencedPropertyCollections()
  {
    return referencedPropertyCollections;
  }
  
  @JsonDeserialize(contentAs = ReferencedPropertyCollectionForPermissionModel.class)
  public void setReferencedPropertyCollections(
      Map<String, IReferencedPropertyCollectionForPermissionModel> referencedPropertyCollections)
  {
    this.referencedPropertyCollections = referencedPropertyCollections;
  }
  
  public Map<String, IConfigEntityInformationModel> getReferencedRelationships()
  {
    return referencedRelationships;
  }
  
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setReferencedRelationships(
      Map<String, IConfigEntityInformationModel> referencedRelationships)
  {
    this.referencedRelationships = referencedRelationships;
  }
  
  public List<IIdLabelCodeModel> getAllowedTemplates()
  {
    if (allowedTemplates == null) {
      allowedTemplates = new ArrayList<>();
    }
    return allowedTemplates;
  }
  
  @JsonDeserialize(contentAs = IdLabelCodeModel.class)
  public void setAllowedTemplates(List<IIdLabelCodeModel> allowedTemplates)
  {
    this.allowedTemplates = allowedTemplates;
  }

}
