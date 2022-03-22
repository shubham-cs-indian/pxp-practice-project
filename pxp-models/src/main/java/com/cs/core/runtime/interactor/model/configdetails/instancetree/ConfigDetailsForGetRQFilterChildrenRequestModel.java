package com.cs.core.runtime.interactor.model.configdetails.instancetree;

import com.cs.core.config.interactor.model.instancetree.ConfigDetailsForGetFilterChildrenRequestModel;

public class ConfigDetailsForGetRQFilterChildrenRequestModel extends ConfigDetailsForGetFilterChildrenRequestModel 
  implements IConfigDetailsForGetRQFilterChildrenRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          type;
  protected String          sideId;
  protected String          relationshipId;
  protected String          targetId;

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
