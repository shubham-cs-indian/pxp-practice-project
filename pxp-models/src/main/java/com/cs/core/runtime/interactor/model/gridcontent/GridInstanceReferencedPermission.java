package com.cs.core.runtime.interactor.model.gridcontent;

import java.util.HashSet;
import java.util.Set;

public class GridInstanceReferencedPermission implements IGridInstanceReferencedPermission {
  
  private static final long serialVersionUID = 1L;
  protected Set<String>     visiblePropertyIds;
  protected Set<String>     editablePropertyIds;
  protected Boolean         isNameVisible    = false;
  protected Boolean         isNameEditable   = false;
  
  @Override
  public Set<String> getVisiblePropertyIds()
  {
    if (visiblePropertyIds == null) {
      return new HashSet<String>();
    }
    return visiblePropertyIds;
  }
  
  @Override
  public void setVisiblePropertyIds(Set<String> visiblePropertyIds)
  {
    this.visiblePropertyIds = visiblePropertyIds;
  }
  
  @Override
  public Set<String> getEditablePropertyIds()
  {
    if (editablePropertyIds == null) {
      return new HashSet<String>();
    }
    return editablePropertyIds;
  }
  
  @Override
  public void setEditablePropertyIds(Set<String> editablePropertyIds)
  {
    this.editablePropertyIds = editablePropertyIds;
  }
  
  @Override
  public Boolean getIsNameEditable()
  {
    return isNameEditable;
  }
  
  @Override
  public void setIsNameEditable(Boolean isNameEditable)
  {
    this.isNameEditable = isNameEditable;
  }
  
  @Override
  public Boolean getIsNameVisible()
  {
    return isNameVisible;
  }
  
  @Override
  public void setIsNameVisible(Boolean isNameVisible)
  {
    this.isNameVisible = isNameVisible;
  }
}
