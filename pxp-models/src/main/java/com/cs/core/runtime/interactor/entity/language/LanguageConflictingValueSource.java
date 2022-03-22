package com.cs.core.runtime.interactor.entity.language;

import com.cs.core.runtime.interactor.entity.datarule.AbstractConflictingValueSource;

public class LanguageConflictingValueSource extends AbstractConflictingValueSource
    implements ILanguageConflictingValueSource {
  
  private static final long serialVersionUID = 1L;
  protected String          contentId;
  
  @Override
  public String getContentId()
  {
    return contentId;
  }
  
  @Override
  public void setContentId(String contentId)
  {
    this.contentId = contentId;
  }
}
