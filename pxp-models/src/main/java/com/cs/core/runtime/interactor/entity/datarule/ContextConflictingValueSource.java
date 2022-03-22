package com.cs.core.runtime.interactor.entity.datarule;

public class ContextConflictingValueSource extends AbstractConflictingValueSource
    implements IContextConflictingValueSource {
  
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
