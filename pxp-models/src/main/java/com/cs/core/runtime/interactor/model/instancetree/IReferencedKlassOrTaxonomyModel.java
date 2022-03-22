package com.cs.core.runtime.interactor.model.instancetree;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeIconModel;

public interface IReferencedKlassOrTaxonomyModel extends IIdLabelCodeIconModel {
  
  public static final String PARENT_ID = "parentId";
  public static final String CHILDREN  = "children";
  
  public String getParentId();
  
  public void setParentId(String parentId);
  
  public List<IReferencedKlassOrTaxonomyModel> getChildren();
  
  public void setChildren(List<IReferencedKlassOrTaxonomyModel> children);
  
}
