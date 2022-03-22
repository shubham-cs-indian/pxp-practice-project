package com.cs.core.config.interactor.entity.template;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

public interface ITemplateHeaderVisibility extends IConfigEntity {
  
  public static final String VIEW_NAME               = "viewName";
  public static final String VIEW_ICON               = "viewIcon";
  public static final String VIEW_PRIMARY_TYPE       = "viewPrimaryType";
  public static final String VIEW_ADDITIONAL_KLASSES = "viewAdditionalClasses";
  public static final String VIEW_TAXONOMIES         = "viewTaxonomies";
  public static final String VIEW_STATUS_TAGS        = "viewStatusTags";
  public static final String VIEW_CREATED_ON         = "viewCreatedOn";
  public static final String VIEW_LASTMODIFIED_BY    = "viewLastModifiedBy";
  
  public Boolean getViewName();
  
  public void setViewName(Boolean viewName);
  
  public Boolean getViewIcon();
  
  public void setViewIcon(Boolean viewIcon);
  
  public Boolean getViewPrimaryType();
  
  public void setViewPrimaryType(Boolean viewPrimartyType);
  
  public Boolean getViewAdditionalClasses();
  
  public void setViewAdditionalClasses(Boolean viewAdditionalClasses);
  
  public Boolean getViewTaxonomies();
  
  public void setViewTaxonomies(Boolean viewTaxonomies);
  
  public Boolean getViewStatusTags();
  
  public void setViewStatusTags(Boolean viewStatusTags);
  
  public Boolean getViewCreatedOn();
  
  public void setViewCreatedOn(Boolean viewCreatedOn);
  
  public Boolean getViewLastModifiedBy();
  
  public void setViewLastModifiedBy(Boolean viewLastModifiedBy);
}
