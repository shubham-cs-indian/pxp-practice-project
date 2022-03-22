package com.cs.core.runtime.interactor.entity.klassinstance;

public interface IArticleInstance extends IContentInstance {
  
  public static final String IS_MERGED = "isMerged";
  
  public Boolean getIsMerged();
  
  public void setIsMerged(Boolean isMerged);
}
