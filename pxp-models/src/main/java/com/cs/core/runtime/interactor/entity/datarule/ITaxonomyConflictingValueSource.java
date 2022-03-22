package com.cs.core.runtime.interactor.entity.datarule;

public interface ITaxonomyConflictingValueSource extends IConflictingValueSource {
  
  public static final String KLASS_VERSION_ID = "klassVersionId";
  public static final String CONTENT_ID       = "contentId";
  // public static final String VARIANT_ID = "variantId";
  
  public String getContentId();
  
  public void setContentId(String contentId);
  
  public String getKlassVersionId();
  
  public void setKlassVersionId(String klassVersionId);
}
