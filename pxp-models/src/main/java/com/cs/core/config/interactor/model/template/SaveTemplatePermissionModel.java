package com.cs.core.config.interactor.model.template;

import com.cs.core.config.interactor.entity.template.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class SaveTemplatePermissionModel extends TemplatePermission
    implements ISaveTemplatePermissionModel {
  
  private static final long                     serialVersionUID = 1L;
  
  protected String                              templateId;
  protected String                              roleId;
  protected List<ITabPermission>                addedTabPermission;
  protected List<ITabPermission>                modifiedTabPermission;
  protected List<IPropertyCollectionPermission> addedPropertyCollectionPermissions;
  protected List<IPropertyCollectionPermission> modifiedPropertyCollectionPermissions;
  protected List<IPropertyPermission>           addedPropertyPermissions;
  protected List<IPropertyPermission>           modifiedPropertyPermissions;
  protected List<IRelationshipPermission>       addedRelationshipPermissions;
  protected List<IRelationshipPermission>       modifiedRelationshipPermissions;
  protected String                              entityType;
  protected List<String>                        addedAllowedTemplates;
  protected List<IReferencePermission>          modifiedReferencePermissions;
  List<String>                                  deletedAllowedTemplates;
  
  @Override
  public List<ITabPermission> getAddedTabPermissions()
  {
    if (addedTabPermission == null) {
      return new ArrayList<ITabPermission>();
    }
    return addedTabPermission;
  }
  
  @Override
  @JsonDeserialize(contentAs = TabPermission.class)
  public void setAddedTabPermissions(List<ITabPermission> tabPermission)
  {
    this.addedTabPermission = tabPermission;
  }
  
  @Override
  public List<ITabPermission> getModifiedTabPermissions()
  {
    if (modifiedTabPermission == null) {
      return new ArrayList<ITabPermission>();
    }
    return modifiedTabPermission;
  }
  
  @Override
  @JsonDeserialize(contentAs = TabPermission.class)
  public void setModifiedTabPermissions(List<ITabPermission> tabPermission)
  {
    this.modifiedTabPermission = tabPermission;
  }
  
  @Override
  public List<IPropertyCollectionPermission> getAddedPropertyCollectionPermissions()
  {
    if (addedPropertyCollectionPermissions == null) {
      return new ArrayList<IPropertyCollectionPermission>();
    }
    return addedPropertyCollectionPermissions;
  }
  
  @Override
  @JsonDeserialize(contentAs = PropertyCollectionPermission.class)
  public void setAddedPropertyCollectionPermissions(
      List<IPropertyCollectionPermission> propertyCollectionPermission)
  {
    this.addedPropertyCollectionPermissions = propertyCollectionPermission;
  }
  
  @Override
  public List<IPropertyCollectionPermission> getModifiedPropertyCollectionPermissions()
  {
    if (modifiedPropertyCollectionPermissions == null) {
      return new ArrayList<IPropertyCollectionPermission>();
    }
    return modifiedPropertyCollectionPermissions;
  }
  
  @Override
  @JsonDeserialize(contentAs = PropertyCollectionPermission.class)
  public void setModifiedPropertyCollectionPermissions(
      List<IPropertyCollectionPermission> propertyCollectionPermission)
  {
    this.modifiedPropertyCollectionPermissions = propertyCollectionPermission;
  }
  
  @Override
  public List<IPropertyPermission> getAddedPropertyPermissions()
  {
    if (addedPropertyPermissions == null) {
      return new ArrayList<IPropertyPermission>();
    }
    return addedPropertyPermissions;
  }
  
  @Override
  @JsonDeserialize(contentAs = PropertyPermission.class)
  public void setAddedPropertyPermissions(List<IPropertyPermission> propertyPermission)
  {
    this.addedPropertyPermissions = propertyPermission;
  }
  
  @Override
  public List<IPropertyPermission> getModifiedPropertyPermissions()
  {
    if (modifiedPropertyPermissions == null) {
      return new ArrayList<IPropertyPermission>();
    }
    return modifiedPropertyPermissions;
  }
  
  @Override
  @JsonDeserialize(contentAs = PropertyPermission.class)
  public void setModifiedPropertyPermissions(List<IPropertyPermission> propertyPermission)
  {
    this.modifiedPropertyPermissions = propertyPermission;
  }
  
  @Override
  public List<IRelationshipPermission> getAddedRelationshipPermissions()
  {
    if (addedRelationshipPermissions == null) {
      return new ArrayList<IRelationshipPermission>();
    }
    return addedRelationshipPermissions;
  }
  
  @Override
  @JsonDeserialize(contentAs = RelationshipPermission.class)
  public void setAddedRelationshipPermissions(List<IRelationshipPermission> relationshipPermission)
  {
    this.addedRelationshipPermissions = relationshipPermission;
  }
  
  @Override
  public List<IRelationshipPermission> getModifiedRelationshipPermissions()
  {
    if (modifiedRelationshipPermissions == null) {
      return new ArrayList<IRelationshipPermission>();
    }
    return modifiedRelationshipPermissions;
  }
  
  @Override
  @JsonDeserialize(contentAs = RelationshipPermission.class)
  public void setModifiedRelationshipPermissions(
      List<IRelationshipPermission> relationshipPermission)
  {
    this.modifiedRelationshipPermissions = relationshipPermission;
  }
  
  @Override
  public String getTemplateId()
  {
    return templateId;
  }
  
  @Override
  public void setTemplateId(String templateId)
  {
    this.templateId = templateId;
  }
  
  @Override
  public String getRoleId()
  {
    return roleId;
  }
  
  @Override
  public void setRoleId(String roleId)
  {
    this.roleId = roleId;
  }
  
  @Override
  public String getEntityType()
  {
    return entityType;
  }
  
  @Override
  public void setEntityType(String entityType)
  {
    this.entityType = entityType;
  }
  
  @Override
  public List<String> getAddedAllowedTemplates()
  {
    return addedAllowedTemplates;
  }
  
  @Override
  public void setAddedAllowedTemplates(List<String> addedAllowedTemplates)
  {
    this.addedAllowedTemplates = addedAllowedTemplates;
  }
  
  @Override
  public List<String> getDeletedAllowedTemplates()
  {
    return deletedAllowedTemplates;
  }
  
  @Override
  public void setDeletedAllowedTemplates(List<String> deletedAllowedTemplates)
  {
    this.deletedAllowedTemplates = deletedAllowedTemplates;
  }
  
  @Override
  public List<IReferencePermission> getModifiedReferencePermissions()
  {
    
    if (modifiedReferencePermissions == null) {
      modifiedReferencePermissions = new ArrayList<>();
    }
    return modifiedReferencePermissions;
  }
  
  @JsonDeserialize(contentAs = ReferencePermission.class)
  @Override
  public void setModifiedReferencePermissions(
      List<IReferencePermission> modifiedReferencePermissions)
  {
    this.modifiedReferencePermissions = modifiedReferencePermissions;
  }
}
