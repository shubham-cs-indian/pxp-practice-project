package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassTaxonomyTreeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class KlassTaxonomyTreeModel extends ConfigEntityInformationModel
    implements IKlassTaxonomyTreeModel {
  
  private static final long               serialVersionUID = 1L;
  
  protected String                        icon;
  protected List<IKlassTaxonomyTreeModel> children;
  
  @Override
  public String getIcon()
  {
    return icon;
  }
  
  @Override
  public void setIcon(String icon)
  {
    this.icon = icon;
  }
  
  @Override
  public List<IKlassTaxonomyTreeModel> getChildren()
  {
    return children;
  }
  
  @JsonDeserialize(contentAs = KlassTaxonomyTreeModel.class)
  @Override
  public void setChildren(List<IKlassTaxonomyTreeModel> children)
  {
    this.children = children;
  }
}
