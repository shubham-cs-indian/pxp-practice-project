package com.cs.core.config.interactor.entity.propertycollection;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

import java.util.List;

public interface ISectionPermission extends IConfigEntity {
  
  public static final String IS_HIDDEN         = "isHidden";
  public static final String IS_COLLAPSED      = "isCollapsed";
  public static final String DISABLED_ELEMENTS = "disabledElements";
  
  public Boolean getIsHidden();
  
  public void setIsHidden(Boolean isHidden);
  
  public Boolean getIsCollapsed();
  
  public void setIsCollapsed(Boolean isCollapsed);
  
  public List<String> getDisabledElements();
  
  public void setDisabledElements(List<String> disabledElements);
}
