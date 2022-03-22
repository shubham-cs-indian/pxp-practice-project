package com.cs.core.config.interactor.entity.role;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;
import com.cs.core.config.interactor.entity.propertycollection.ISectionPermission;

import java.util.List;
import java.util.Map;

public interface IRolePermission extends IConfigEntity {
  
  public static final String VISIBLE_STRUCTURE_VIEWS = "visibleStructureViews";
  public static final String SECTION_PERMISSION      = "sectionPermission";
  
  public Map<String, ISectionPermission> getSectionPermission();
  
  public void setSectionPermission(Map<String, ISectionPermission> sectionPermission);
  
  public List<String> getVisibleStructureViews();
  
  public void setVisibleStructureViews(List<String> allowedContentStructureViews);
}
