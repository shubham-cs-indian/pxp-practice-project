package com.cs.core.runtime.interactor.model.gridcontent;

import com.cs.core.config.interactor.model.klass.AbstractReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

public class GridInstanceConfigDetailsModel implements IGridInstanceConfigDetailsModel {
  
  private static final long                             serialVersionUID = 1L;
  protected Map<String, IReferencedSectionElementModel> referencedElements;
  protected IContentInstance                            klassInstance;
  protected IGridInstanceReferencedPermission           referencedPermissions;
  
  @Override
  public Map<String, IReferencedSectionElementModel> getReferencedElements()
  {
    return referencedElements;
  }
  
  @Override
  @JsonDeserialize(contentAs = AbstractReferencedSectionElementModel.class)
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements)
  {
    if (referencedElements == null) {
      referencedElements = new HashMap<>();
    }
    this.referencedElements = referencedElements;
  }
  
  @Override
  public IGridInstanceReferencedPermission getReferencedPermissions()
  {
    return referencedPermissions;
  }
  
  @Override
  @JsonDeserialize(as = GridInstanceReferencedPermission.class)
  public void setReferencedPermissions(IGridInstanceReferencedPermission referencedPermissions)
  {
    this.referencedPermissions = referencedPermissions;
  }
}
