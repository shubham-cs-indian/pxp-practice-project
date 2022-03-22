package com.cs.core.config.interactor.model.language;

import java.util.ArrayList;
import java.util.List;

public class LanguageHierarchyModel implements ILanguageHierarchyModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    parents;
  protected List<String>    children;
  
  @Override
  public List<String> getParents()
  {
    if (parents == null) {
      parents = new ArrayList<>();
    }
    return parents;
  }
  
  @Override
  public void setParents(List<String> parents)
  {
    this.parents = parents;
  }
  
  @Override
  public List<String> getChildren()
  {
    if (children == null) {
      children = new ArrayList<>();
    }
    return children;
  }
  
  @Override
  public void setChildren(List<String> children)
  {
    this.children = children;
  }
}
