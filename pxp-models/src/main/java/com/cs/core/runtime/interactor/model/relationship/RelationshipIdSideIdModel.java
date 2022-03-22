package com.cs.core.runtime.interactor.model.relationship;

public class RelationshipIdSideIdModel implements IRelationshipIdSideIdModel {
  
  private static final long serialVersionUID = 1L;
  protected String          sideId;
  protected String          relationshipId;
  
  @Override
  public String getSideId()
  {
    return sideId;
  }
  
  @Override
  public void setSideId(String sideId)
  {
    this.sideId = sideId;
  }
  
  @Override
  public String getRelationshipId()
  {
    return relationshipId;
  }
  
  @Override
  public void setRelationshipId(String relationshipId)
  {
    this.relationshipId = relationshipId;
  }
}
