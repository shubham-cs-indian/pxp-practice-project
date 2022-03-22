package com.cs.core.runtime.interactor.model.configdetails.instancetree;

public class ConfigDetailsForRelationshipQuicklistRequestModel
    extends GetConfigDetailsForGetNewInstanceTreeRequestModel
    implements IConfigDetailsForRelationshipQuicklistRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          sideId;
  protected String          relationshipId;
  protected String          targetId;
  
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
  
  @Override
  public String getTargetId()
  {
    return targetId;
  }
  
  @Override
  public void setTargetId(String targetId)
  {
    this.targetId = targetId;
  }
}
