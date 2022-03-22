package com.cs.core.config.interactor.entity.template;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class TemplatePermission implements ITemplatePermission {
  
  private static final long                            serialVersionUID = 1L;
  
  protected Map<String, ITabPermission>                tabPermission;
  protected IHeaderPermission                          headerPermission;
  protected Map<String, IPropertyCollectionPermission> propertyCollectionPermission;
  protected Map<String, IPropertyPermission>           propertyPermissions;
  protected Map<String, IRelationshipPermission>       relationshipPermissions;
  protected String                                     entityId;
  protected String                                     code;
  
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
  public Map<String, ITabPermission> getTabPermission()
  {
    return tabPermission;
  }
  
  @Override
  @JsonDeserialize(contentAs = TabPermission.class)
  public void setTabPermission(Map<String, ITabPermission> tabPermission)
  {
    this.tabPermission = tabPermission;
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
  
  /**
   * **************************************************** Ignored Properties
   * ********************************************************
   */
  @JsonIgnore
  @Override
  public String getId()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setId(String id)
  {
  }
  
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
  
  @Override
  public String getEntityId()
  {
    return entityId;
  }
  
  @Override
  public void setEntityId(String entityId)
  {
    this.entityId = entityId;
  }
}
