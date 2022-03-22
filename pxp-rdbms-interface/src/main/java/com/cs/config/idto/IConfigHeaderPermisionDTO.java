package com.cs.config.idto;

/**
 * gHeaderPermision DTO from the configuration realm
 * @author mangesh.metkari
 *
 */
public interface IConfigHeaderPermisionDTO extends IConfigJSONDTO {
  
 public String getEntityId();
  
  public void setEntityId(String entityId);
  
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
