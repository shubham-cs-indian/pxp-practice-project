package com.cs.core.runtime.interactor.model.gridcontent;

import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IGridInstanceConfigDetailsModel extends IModel {
  
  public static final String REFERENCED_ELEMENTS    = "referencedElements";
  public static final String REFERENCED_PERMISSIONS = "referencedPermissions";
  
  public Map<String, IReferencedSectionElementModel> getReferencedElements();
  
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements);
  
  public IGridInstanceReferencedPermission getReferencedPermissions();
  
  public void setReferencedPermissions(IGridInstanceReferencedPermission referencedPermissions);
}
