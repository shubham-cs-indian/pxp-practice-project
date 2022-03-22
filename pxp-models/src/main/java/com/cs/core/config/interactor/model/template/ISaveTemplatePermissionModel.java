package com.cs.core.config.interactor.model.template;

import com.cs.core.config.interactor.entity.template.*;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ISaveTemplatePermissionModel extends ITemplatePermission, IModel {
  
  public static final String HEADER_PERMISSION                       = "headerPermission";
  public static final String ADDED_TAB_PERMISSION                    = "addedTabPermissions";
  public static final String ADDED_PROPERTY_COLLECTION_PERMISSION    = "addedPropertyCollectionPermissions";
  public static final String ADDED_PROPERTY_PERMISSION               = "addedPropertyPermissions";
  public static final String ADDED_RELATIONSHIP_PERMISSION           = "addedRelationshipPermissions";
  public static final String MODIFIED_TAB_PERMISSION                 = "modifiedTabPermissions";
  public static final String MODIFIED_PROPERTY_COLLECTION_PERMISSION = "modifiedPropertyCollectionPermissions";
  public static final String MODIFIED_PROPERTY_PERMISSION            = "modifiedPropertyPermissions";
  public static final String MODIFIED_RELATIONSHIP_PERMISSION        = "modifiedRelationshipPermissions";
  public static final String TEMPLATE_ID                             = "templateId";
  public static final String ROLE_ID                                 = "roleId";
  public static final String ENTITY_TYPE                             = "entityType";
  public static final String ADDED_ALLOWED_TEMPLATES                 = "addedAllowedTemplates";
  public static final String DELETED_ALLOWED_TEMPLATES               = "deletedAllowedTemplates";
  public static final String MODIFIED_REFERENCE_PERMISSIONS          = "modifiedReferencePermissions";
  
  public List<ITabPermission> getAddedTabPermissions();
  
  public void setAddedTabPermissions(List<ITabPermission> tabPermission);
  
  public List<ITabPermission> getModifiedTabPermissions();
  
  public void setModifiedTabPermissions(List<ITabPermission> tabPermission);
  
  public List<IPropertyCollectionPermission> getAddedPropertyCollectionPermissions();
  
  public void setAddedPropertyCollectionPermissions(
      List<IPropertyCollectionPermission> propertyCollectionPermission);
  
  public List<IPropertyCollectionPermission> getModifiedPropertyCollectionPermissions();
  
  public void setModifiedPropertyCollectionPermissions(
      List<IPropertyCollectionPermission> propertyCollectionPermission);
  
  public List<IPropertyPermission> getAddedPropertyPermissions();
  
  public void setAddedPropertyPermissions(List<IPropertyPermission> propertyPermission);
  
  public List<IPropertyPermission> getModifiedPropertyPermissions();
  
  public void setModifiedPropertyPermissions(List<IPropertyPermission> propertyPermission);
  
  public List<IRelationshipPermission> getAddedRelationshipPermissions();
  
  public void setAddedRelationshipPermissions(List<IRelationshipPermission> relationshipPermission);
  
  public List<IRelationshipPermission> getModifiedRelationshipPermissions();
  
  public void setModifiedRelationshipPermissions(
      List<IRelationshipPermission> relationshipPermission);
  
  public String getTemplateId();
  
  public void setTemplateId(String templateId);
  
  public String getRoleId();
  
  public void setRoleId(String roleId);
  
  public String getEntityType();
  
  public void setEntityType(String entityType);
  
  public List<String> getAddedAllowedTemplates();
  
  public void setAddedAllowedTemplates(List<String> addedAllowedTemplates);
  
  public List<String> getDeletedAllowedTemplates();
  
  public void setDeletedAllowedTemplates(List<String> deletedAllowedTemplates);
  
  public List<IReferencePermission> getModifiedReferencePermissions();
  
  public void setModifiedReferencePermissions(
      List<IReferencePermission> modifiedReferencePermissions);
}
