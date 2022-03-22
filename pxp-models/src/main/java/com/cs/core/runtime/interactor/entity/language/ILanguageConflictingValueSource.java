package com.cs.core.runtime.interactor.entity.language;

import com.cs.core.runtime.interactor.entity.datarule.IConflictingValueSource;

public interface ILanguageConflictingValueSource extends IConflictingValueSource {
  
  public static final String CONTENT_ID       = "contentId";
  
  public String getContentId();
  
  public void setContentId(String contentId);
}
