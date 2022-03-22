package com.cs.core.runtime.interactor.entity.relationship;

import com.cs.core.runtime.interactor.entity.datarule.IConflictingValueSource;

public interface IRelationshipConflictingValueSource extends IConflictingValueSource {
  
  public static final String CONTENT_VERIONID = "contentVersionId";
  public static final String CONTENT_ID       = "contentId";
  // public static final String VARIANT_ID = "variantId";
  
  public String getContentId();
  
  public void setContentId(String contentId);
  
  public String getContentVersionId();
  
  public void setContentVersionId(String contentVersionId);
  
  /*  public String getVariantId();
  public void setVariantId(String variantId);*/
}
