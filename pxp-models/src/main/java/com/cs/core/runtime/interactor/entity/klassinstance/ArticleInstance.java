package com.cs.core.runtime.interactor.entity.klassinstance;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NONE)
public class ArticleInstance extends AbstractContentInstance implements IArticleInstance {
  
  private static final long serialVersionUID = 1L;
  protected Boolean         isMerged;
  
  @Override
  public Boolean getIsMerged()
  {
    if (isMerged == null) {
      isMerged = false;
    }
    return isMerged;
  }
  
  @Override
  public void setIsMerged(Boolean isMerged)
  {
    this.isMerged = isMerged;
  }
}
