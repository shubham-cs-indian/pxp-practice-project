package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;

import java.util.List;

public interface IKlassTaxonomyTreeModel extends IConfigEntityInformationModel {
  
  public static final String ICON     = "icon";
  public static final String CHILDREN = "children";
  
  public String getIcon();
  
  public void setIcon(String icon);
  
  public List<IKlassTaxonomyTreeModel> getChildren();
  
  public void setChildren(List<IKlassTaxonomyTreeModel> children);
}
