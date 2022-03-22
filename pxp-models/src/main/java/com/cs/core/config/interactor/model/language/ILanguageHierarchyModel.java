package com.cs.core.config.interactor.model.language;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ILanguageHierarchyModel extends IModel {
  
  public static final String PARENTS  = "parents";
  public static final String CHILDREN = "children";
  
  public List<String> getParents();
  
  public void setParents(List<String> parents);
  
  public List<String> getChildren();
  
  public void setChildren(List<String> children);
}
