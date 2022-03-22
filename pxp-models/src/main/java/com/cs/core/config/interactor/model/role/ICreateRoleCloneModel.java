package com.cs.core.config.interactor.model.role;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ICreateRoleCloneModel extends IModel{
  
  String IS_EXACT_CLONE          = "isExactClone";
  String LABEL                   = "label";
  String CODE                    = "code";
  String ROLE_ID                 = "roleId";
  String CLONE_PHYSICAL_CATALOGS = "clonePhysicalCatalogs";
  String CLONE_TAXONOMIES        = "cloneTaxonomies";
  String CLONE_TARGET_CLASSES    = "cloneTargetClasses";
  String CLONE_ENABLE_DASHBOARD  = "cloneEnableDashboard";
  String CLONE_KPI               = "cloneKPI";
  String CLONE_ENTITIES          = "cloneEntities";
  
  Boolean getIsExactClone();
  void setIsExactClone(Boolean isExactClone);
  
  String getLabel();
  void setLabel(String label);
  
  String getCode();
  void setCode(String code);
  
  String getRoleId();
  void setRoleId(String roleId);
  
  Boolean getClonePhysicalCatalogs();
  void setClonePhysicalCatalogs(Boolean clonePhysicalCatalogs);
  
  Boolean getCloneTaxonomies();
  void setCloneTaxonomies(Boolean cloneTaxonomies);
  
  Boolean getCloneTargetClasses();
  void setCloneTargetClasses(Boolean cloneTargetClasses);
  
  Boolean getCloneEnableDashboard();
  void setCloneEnableDashboard(Boolean cloneEnableDashboard);
  
  Boolean getCloneKPI();
  void setCloneKPI(Boolean cloneKPI);
  
  Boolean getCloneEntities();
  void setCloneEntities(Boolean cloneEntities);
}
