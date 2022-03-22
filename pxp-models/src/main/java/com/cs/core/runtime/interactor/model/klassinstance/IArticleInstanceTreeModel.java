package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.entity.klassinstance.IArticleInstance;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IArticleInstanceTreeModel extends IModel {
  
  public static final String ARTICLE_INSTANCE = "articleInstance";
  public static final String CHILDREN         = "children";
  public static final String TREE_ELEMENTS    = "treeElements";
  
  public IArticleInstance getArticleInstance();
  
  public void setArticleInstance(IArticleInstance articleInstance);
  
  public List<IKlassInstanceInformationModel> getChildren();
  
  public void setChildren(List<IKlassInstanceInformationModel> childrens);
  
  public List<IKlassInstanceTreeInformationModel> getTreeElements();
  
  public void setTreeElements(List<IKlassInstanceTreeInformationModel> treeElements);
}
