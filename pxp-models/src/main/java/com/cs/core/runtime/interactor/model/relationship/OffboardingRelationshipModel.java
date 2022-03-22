package com.cs.core.runtime.interactor.model.relationship;

public class OffboardingRelationshipModel implements IOffboardingRelationshipModel {
  
  private static final long serialVersionUID = 1L;
  String                    sourceId;
  String                    destinationId;
  String                    relationshipId;
  
  @Override
  public String getSourceId()
  {
    return this.sourceId;
  }
  
  @Override
  public void setSourceId(String sourceId)
  {
    this.sourceId = sourceId;
  }
  
  @Override
  public String getDestinationId()
  {
    return this.destinationId;
  }
  
  @Override
  public void setDestinationId(String destinationId)
  {
    this.destinationId = destinationId;
  }
  
  @Override
  public String getRelationshipId()
  {
    return this.relationshipId;
  }
  
  @Override
  public void setRelationshipId(String relationshipId)
  {
    this.relationshipId = relationshipId;
  }
  
  @Override
  public String toString()
  {
    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("{");
    strBuilder.append("\"relationshipId\":" + "\"" + relationshipId + "\",");
    strBuilder.append("\"sourceId\":" + "\"" + sourceId + "\",");
    strBuilder.append("\"destinationId\":" + "\"" + destinationId + "\"");
    strBuilder.append("}");
    
    return strBuilder.toString();
  }
}
