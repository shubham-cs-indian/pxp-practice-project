package com.cs.core.config.interactor.model.permission;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;

public class ReferencedPropertyCollectionForPermissionModel extends IdLabelCodeModel implements IReferencedPropertyCollectionForPermissionModel{

  private static final long serialVersionUID = 1L;
  private List<String>      elementIds;

  @Override
  public List<String> getElementIds()
  {
    if (elementIds == null) {
      elementIds = new ArrayList<>();
    }
    return elementIds;
  }

  @Override
  public void setElementIds(List<String> elementIds)
  {
    this.elementIds = elementIds;
  }
  
  
}
