package com.cs.core.runtime.interactor.model.configdetails.instancetree;

public class ConfigDetailsForRQFilterAndSortDataRequestModel extends ConfigDetailsForFilterAndSortInfoRequestModel implements IConfigDetailsForRQFilterAndSortDataRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          userId;
  protected String          sideId;
  protected String          relationshipId;
  protected String          targetId;
  protected String          type;
  
  @Override
  public String getUserId()
  {
    return userId;
  }
  
  @Override
  public void setUserId(String userId)
  {
    this.userId = userId;
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

  @Override
  public String getType()
  {
    return type;
  }

  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
}
