package com.cs.core.runtime.interactor.model.goldenrecord;

public class RelationshipIdSourceModel implements IRelationshipIdSourceModel {
  
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  
  protected String          relationshipId;
  protected String          sourceKlassInstanceId;
  protected String          sideId;
  
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
  
  @Override
  public String getSourceKlassInstanceId()
  {
    return sourceKlassInstanceId;
  }
  
  @Override
  public void setSourceKlassInstanceId(String sourceKlassInstanceId)
  {
    this.sourceKlassInstanceId = sourceKlassInstanceId;
  }
  
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
}
