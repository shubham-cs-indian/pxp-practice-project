package com.cs.core.runtime.interactor.entity.datarule;

public class TaxonomyConflictingValueSource extends AbstractConflictingValueSource
    implements ITaxonomyConflictingValueSource {
  
  private static final long serialVersionUID = 1L;
  
  protected String          klassVersionId;
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
  
  
  @Override
  public String getKlassVersionId()
  {
    return klassVersionId;
  }
  
  @Override
  public void setKlassVersionId(String klassVersionId)
  {
    this.klassVersionId = klassVersionId;
  }
}
