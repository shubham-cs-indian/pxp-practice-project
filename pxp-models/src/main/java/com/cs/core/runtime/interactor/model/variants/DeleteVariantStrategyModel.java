package com.cs.core.runtime.interactor.model.variants;

public class DeleteVariantStrategyModel extends DeleteVariantModel
    implements IDeleteVariantStrategyModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          natureRelationshipId;
  
  @Override
  public String getNatureRelationshipId()
  {
    return natureRelationshipId;
  }
  
  @Override
  public void setNatureRelationshipId(String natureRelationshipId)
  {
    this.natureRelationshipId = natureRelationshipId;
  }
}
