package com.cs.core.config.interactor.model.permission;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;

public interface IReferencedPropertyCollectionForPermissionModel extends IIdLabelCodeModel {
  
  public static final String ELEMENT_IDS  = "elementIds";
  
  public List<String> getElementIds();
  
  public void setElementIds(List<String> elementIds);
}
