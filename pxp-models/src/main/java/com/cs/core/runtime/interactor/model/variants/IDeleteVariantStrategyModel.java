package com.cs.core.runtime.interactor.model.variants;

public interface IDeleteVariantStrategyModel extends IDeleteVariantModel {
  
  public static final String NATURE_RELATIONSHIP_ID = "natureRelationshipId";
  
  public String getNatureRelationshipId();
  
  public void setNatureRelationshipId(String natureRelationshipId);
}
