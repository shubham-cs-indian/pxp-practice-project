package com.cs.core.runtime.interactor.entity.searchable;

public class ArticleSearchableInstance extends AbstractSearchableInstance
    implements IArticleSearchableInstance {
  
  private static final long serialVersionUID = 1L;
  protected String          isMerged;
  
  public String getIsMerged()
  {
    return isMerged;
  }
  
  public void setIsMerged(String isMerged)
  {
    this.isMerged = isMerged;
  }
}
