package com.cs.core.runtime.interactor.model.taxonomy;

public interface IGetTaxonomyTreeForQuicklistModel extends IGetTaxonomyTreeModel {
  
  public static final String RELATIONSHIP_ID = "relationshipId";
  public static final String TYPE_KLASS_ID   = "typeKlassId";
  public static final String TARGET_TYPE     = "targetType";
  public static final String SIDE_ID         = "sideId";
  public static final String REFERENCE_ID    = "referenceId";
  
  public String getRelationshipId();
  
  public void setRelationshipId(String relationshipId);
  
  public String getTypeKlassId();
  
  public void setTypeKlassId(String typeKlassId);
  
  public String getTargetType();
  
  public void setTargetType(String targetType);
  
  public String getSideId();
  
  public void setSideId(String sideId);
  
  public String getReferenceId();
  
  public void setReferenceId(String relationshipId);
}
