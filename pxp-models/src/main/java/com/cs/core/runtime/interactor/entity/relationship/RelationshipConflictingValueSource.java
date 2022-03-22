package com.cs.core.runtime.interactor.entity.relationship;

import com.cs.core.runtime.interactor.entity.datarule.AbstractConflictingValueSource;

public class RelationshipConflictingValueSource extends AbstractConflictingValueSource
    implements IRelationshipConflictingValueSource {
  
  private static final long serialVersionUID = 1L;
  
  protected String          contentVersionId;
  protected String          contentId;
  // protected String variantId;
  
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
  public String getContentVersionId()
  {
    return contentVersionId;
  }
  
  @Override
  public void setContentVersionId(String contentVersionId)
  {
    this.contentVersionId = contentVersionId;
  }
  
  /* @Override
  public String getVariantId()
  {
    return variantId;
  }
  
  @Override
  public void setVariantId(String variantId)
  {
    this.variantId = variantId;
  }*/
}
