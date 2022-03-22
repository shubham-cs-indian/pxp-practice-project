package com.cs.core.config.interactor.entity.template;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

public interface IHeaderPermission extends IConfigEntity {
  
  public static final String VIEW_NAME               = "viewName";
  public static final String CAN_EDIT_NAME           = "canEditName";
  public static final String VIEW_ICON               = "viewIcon";
  public static final String CAN_CHANGE_ICON         = "canChangeIcon";
  public static final String VIEW_PRIMARY_TYPE       = "viewPrimaryType";
  public static final String CAN_EDIT_PRIMARY_TYPE   = "canEditPrimaryType";
  public static final String VIEW_ADDITIONAL_CLASSES = "viewAdditionalClasses";
  public static final String CAN_ADD_CLASSES         = "canAddClasses";
  public static final String CAN_DELETE_CLASSES      = "canDeleteClasses";
  public static final String VIEW_TAXONOMIES         = "viewTaxonomies";
  public static final String CAN_ADD_TAXONOMY        = "canAddTaxonomy";
  public static final String CAN_DELETE_TAXONOMY     = "canDeleteTaxonomy";
  public static final String VIEW_STATUS_TAGS        = "viewStatusTags";
  public static final String CAN_EDIT_STATUS_TAG     = "canEditStatusTag";
  public static final String VIEW_CREATED_ON         = "viewCreatedOn";
  public static final String VIEW_LAST_MODIFIED_BY   = "viewLastModifiedBy";
  public static final String ENTITY_ID               = "entityId";
  public static final String ROLE_ID                 = "roleId";
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
  
  public String getRoleId();
  
  public void setRoleId(String roleId);
  
  public Boolean getViewName();
  
  public void setViewName(Boolean viewName);
  
  public Boolean getCanEditName();
  
  public void setCanEditName(Boolean canEditName);
  
  public Boolean getViewIcon();
  
  public void setViewIcon(Boolean viewIcon);
  
  public Boolean getCanChangeIcon();
  
  public void setCanChangeIcon(Boolean canChangeIcon);
  
  public Boolean getViewPrimaryType();
  
  public void setViewPrimaryType(Boolean viewPrimaryType);
  
  public Boolean getCanEditPrimaryType();
  
  public void setCanEditPrimaryType(Boolean canEditPrimaryType);
  
  public Boolean getViewAdditionalClasses();
  
  public void setViewAdditionalClasses(Boolean viewAdditionalClasses);
  
  public Boolean getCanAddClasses();
  
  public void setCanAddClasses(Boolean canAddClasses);
  
  public Boolean getCanDeleteClasses();
  
  public void setCanDeleteClasses(Boolean canDeleteClasses);
  
  public Boolean getViewTaxonomies();
  
  public void setViewTaxonomies(Boolean viewTaxonomies);
  
  public Boolean getCanAddTaxonomy();
  
  public void setCanAddTaxonomy(Boolean canAddTaxonomy);
  
  public Boolean getCanDeleteTaxonomy();
  
  public void setCanDeleteTaxonomy(Boolean canDeleteTaxonomy);
  
  public Boolean getViewStatusTags();
  
  public void setViewStatusTags(Boolean viewStatusTags);
  
  public Boolean getCanEditStatusTag();
  
  public void setCanEditStatusTag(Boolean canEditStatusTag);
  
  public Boolean getViewCreatedOn();
  
  public void setViewCreatedOn(Boolean viewCreatedOn);
  
  public Boolean getViewLastModifiedBy();
  
  public void setViewLastModifiedBy(Boolean viewLastModifiedBy);
}
