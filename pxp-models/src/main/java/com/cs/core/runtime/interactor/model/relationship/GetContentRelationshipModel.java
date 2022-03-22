package com.cs.core.runtime.interactor.model.relationship;

public class GetContentRelationshipModel implements IGetContentRelationshipModel {
  
  protected String id;
  protected String relationshipMappingId;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getRelationshipMappingId()
  {
    return relationshipMappingId;
  }
  
  @Override
  public void setRelationshipMappingId(String relationshipMappingId)
  {
    this.relationshipMappingId = relationshipMappingId;
  }
}
