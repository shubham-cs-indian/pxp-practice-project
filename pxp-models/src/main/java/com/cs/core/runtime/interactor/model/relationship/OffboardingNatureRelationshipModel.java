package com.cs.core.runtime.interactor.model.relationship;

public class OffboardingNatureRelationshipModel extends OffboardingRelationshipModel
    implements IOffboardingNatureRelationshipModel {
  
  private static final long serialVersionUID = 1L;
  String                    count;
  
  @Override
  public String getCount()
  {
    return this.count;
  }
  
  @Override
  public void setCount(String count)
  {
    this.count = count;
  }
  
  @Override
  public String toString()
  {
    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("{");
    strBuilder.append("\"relationshipId\":" + "\"" + relationshipId + "\",");
    strBuilder.append("\"sourceId\":" + "\"" + sourceId + "\",");
    strBuilder.append("\"destinationId\":" + "\"" + destinationId + "\",");
    strBuilder.append("\"count\":" + "\"" + count + "\"");
    strBuilder.append("}");
    
    return strBuilder.toString();
  }
}
