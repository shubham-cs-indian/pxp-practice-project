package com.cs.core.runtime.interactor.entity.searchable;

public interface IArticleSearchableInstance extends ISearchableInstance {
  
  public static final String IS_MERGED = "isMerged";
  
  public String getIsMerged();
  
  public void setIsMerged(String isMerged);
}
