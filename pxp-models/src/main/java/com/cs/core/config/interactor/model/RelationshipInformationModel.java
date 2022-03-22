package com.cs.core.config.interactor.model;

public class RelationshipInformationModel implements IRelationshipInformationModel {
  
  private static final long serialVersionUID = 1L;
  protected String          relationshipId;
  protected String          relationshipType;
  protected String          relationshipSide1Id;
  protected String          relationshipSide2Id;
  protected String          sourceSideId;
  protected String          targetSideId;
  
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
  public String getRelationshipSide1Id()
  {
    return relationshipSide1Id;
  }
  
  @Override
  public void setRelationshipSide1Id(String relationshipSide1Id)
  {
    this.relationshipSide1Id = relationshipSide1Id;
  }
  
  @Override
  public String getRelationshipSide2Id()
  {
    return relationshipSide2Id;
  }
  
  @Override
  public void setRelationshipSide2Id(String relationshipSide2Id)
  {
    this.relationshipSide2Id = relationshipSide2Id;
  }
  
  @Override
  public String getRelationshipType()
  {
    return relationshipType;
  }
  
  @Override
  public void setRelationshipType(String relationshipType)
  {
    this.relationshipType = relationshipType;
  }
  
  @Override
  public String getSourceSideId()
  {
    return sourceSideId;
  }
  
  @Override
  public void setSourceSideId(String sourceSideId)
  {
    this.sourceSideId = sourceSideId;
  }
  
  @Override
  public String getTargetSideId()
  {
    return targetSideId;
  }
  
  @Override
  public void setTargetSideId(String targetSideId)
  {
    this.targetSideId = targetSideId;
  }
}
