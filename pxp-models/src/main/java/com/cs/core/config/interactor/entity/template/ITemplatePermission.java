package com.cs.core.config.interactor.entity.template;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

import java.util.Map;

public interface ITemplatePermission extends IConfigEntity {
  
  public static final String TAB_PERMISSION                 = "tabPermission";
  public static final String HEADER_PERMISSION              = "headerPermission";
  public static final String PROPERTY_COLLECTION_PERMISSION = "propertyCollectionPermission";
  public static final String PROPERTY_PERMISSION            = "propertyPermission";
  public static final String RELATIONSHIP_PERMISSION        = "relationshipPermission";
  public static final String ENTITY_ID                      = "entityId";
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
  
  public Map<String, ITabPermission> getTabPermission();
  
  public void setTabPermission(Map<String, ITabPermission> tabPermission);
  
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
}
