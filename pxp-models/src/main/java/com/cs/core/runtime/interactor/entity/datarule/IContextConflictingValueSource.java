package com.cs.core.runtime.interactor.entity.datarule;

public interface IContextConflictingValueSource extends IConflictingValueSource {
  
  public static final String CONTENT_ID = "contentId";
  
  public String getContentId();
  
  public void setContentId(String contentId);
}
