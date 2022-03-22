package com.cs.core.runtime.interactor.model.gridcontent;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Set;

public interface IGridInstanceReferencedPermission extends IModel {
  
  public static final String VISIBLE_PROPERTY_IDS  = "visiblePropertyIds";
  public static final String EDITABLE_PROPERTY_IDS = "editablePropertyIds";
  public static final String IS_NAME_EDITABLE      = "isNameEditable";
  public static final String IS_NAME_VISIBLE       = "isNameVisible";
  
  public Set<String> getVisiblePropertyIds();
  
  public void setVisiblePropertyIds(Set<String> visiblePropertyIds);
  
  public Set<String> getEditablePropertyIds();
  
  public void setEditablePropertyIds(Set<String> editablePropertyIds);
  
  public Boolean getIsNameEditable();
  
  public void setIsNameEditable(Boolean isNameEditable);
  
  public Boolean getIsNameVisible();
  
  public void setIsNameVisible(Boolean isNameVisible);
}
