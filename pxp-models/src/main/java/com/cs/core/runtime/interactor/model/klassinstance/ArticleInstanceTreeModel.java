package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.entity.klassinstance.ArticleInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IArticleInstance;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

@JsonTypeInfo(use = Id.NONE)
public class ArticleInstanceTreeModel implements IArticleInstanceTreeModel {
  
  private static final long                          serialVersionUID = 1L;
  
  protected IArticleInstance                         articleInstance;
  
  protected List<IKlassInstanceInformationModel>     children;
  
  protected List<IKlassInstanceTreeInformationModel> treeElements;
  
  @Override
  public List<IKlassInstanceInformationModel> getChildren()
  {
    if (children == null) {
      this.children = new ArrayList<>();
    }
    return this.children;
  }
  
  @JsonDeserialize(contentAs = KlassInstanceInformationModel.class)
  @Override
  public void setChildren(List<IKlassInstanceInformationModel> childrens)
  {
    this.children = childrens;
  }
  
  @Override
  public List<IKlassInstanceTreeInformationModel> getTreeElements()
  {
    return treeElements;
  }
  
  @JsonDeserialize(contentAs = KlassInstanceTreeInformationModel.class)
  @Override
  public void setTreeElements(List<IKlassInstanceTreeInformationModel> treeElements)
  {
    this.treeElements = treeElements;
  }
  
  @Override
  public IArticleInstance getArticleInstance()
  {
    return this.articleInstance;
  }
  
  @JsonTypeInfo(use = Id.NONE)
  @JsonDeserialize(as = ArticleInstance.class)
  @Override
  public void setArticleInstance(IArticleInstance articleInstance)
  {
    this.articleInstance = articleInstance;
  }
}
