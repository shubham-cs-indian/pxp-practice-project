package com.cs.core.config.interactor.entity.permission;

import com.cs.core.config.interactor.entity.globalpermissions.GlobalPermission;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.template.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class Permission implements IPermission {
  
  private static final long                            serialVersionUID = 1L;
  
  protected IHeaderPermission                          headerPermission;
  protected Map<String, IPropertyCollectionPermission> propertyCollectionPermission;
  protected Map<String, IPropertyPermission>           propertyPermissions;
  protected Map<String, IRelationshipPermission>       relationshipPermissions;
  protected Map<String, IReferencePermission>          referencePermissions;
  protected String                                     id;
  protected String                                     label;
  protected String                                     code;
  protected IGlobalPermission                          globalPermission;
  
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
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
  }
  
  @Override
  public IGlobalPermission getGlobalPermission()
  {
    return globalPermission;
  }
  
  @Override
  @JsonDeserialize(as = GlobalPermission.class)
  public void setGlobalPermission(IGlobalPermission globalPermission)
  {
    this.globalPermission = globalPermission;
  }
  
  @Override
  public IHeaderPermission getHeaderPermission()
  {
    return headerPermission;
  }
  
  @Override
  @JsonDeserialize(as = HeaderPermission.class)
  public void setHeaderPermission(IHeaderPermission headerPermission)
  {
    this.headerPermission = headerPermission;
  }
  
  @Override
  public Map<String, IPropertyCollectionPermission> getPropertyCollectionPermission()
  {
    return propertyCollectionPermission;
  }
  
  @Override
  @JsonDeserialize(contentAs = PropertyCollectionPermission.class)
  public void setPropertyCollectionPermission(
      Map<String, IPropertyCollectionPermission> propertyCollectionPermission)
  {
    this.propertyCollectionPermission = propertyCollectionPermission;
  }
  
  @Override
  public Map<String, IPropertyPermission> getPropertyPermission()
  {
    return propertyPermissions;
  }
  
  @Override
  @JsonDeserialize(contentAs = PropertyPermission.class)
  public void setPropertyPermission(Map<String, IPropertyPermission> propertyPermissions)
  {
    this.propertyPermissions = propertyPermissions;
  }
  
  @Override
  public Map<String, IRelationshipPermission> getRelationshipPermission()
  {
    return relationshipPermissions;
  }
  
  @Override
  @JsonDeserialize(contentAs = RelationshipPermission.class)
  public void setRelationshipPermission(
      Map<String, IRelationshipPermission> relationshipPermissions)
  {
    this.relationshipPermissions = relationshipPermissions;
  }
  
  @Override
  public Map<String, IReferencePermission> getReferencePermissions()
  {
    return referencePermissions;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencePermission.class)
  public void setReferencePermissions(Map<String, IReferencePermission> referencePermissions)
  {
    this.referencePermissions = referencePermissions;
  }
  
  /**
   * **************************************************** Ignored Properties
   * ********************************************************
   */
  @JsonIgnore
  @Override
  public Long getVersionId()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setVersionId(Long versionId)
  {
  }
  
  @JsonIgnore
  @Override
  public Long getVersionTimestamp()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
  }
  
  @JsonIgnore
  @Override
  public String getLastModifiedBy()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
  }
}
