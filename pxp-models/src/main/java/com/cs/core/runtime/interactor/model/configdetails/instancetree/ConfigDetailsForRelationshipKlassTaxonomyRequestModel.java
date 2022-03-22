package com.cs.core.runtime.interactor.model.configdetails.instancetree;

import java.util.ArrayList;
import java.util.List;

public class ConfigDetailsForRelationshipKlassTaxonomyRequestModel extends ConfigDetailsForGetKlassTaxonomyTreeRequestModel
      implements IConfigDetailsForRelationshipKlassTaxonomyRequestModel {

  private static final long serialVersionUID = 1L;
  protected List<String>    allowedEntities;
  protected String          sideId;
  protected String          relationshipId;
  protected String          type;                 
  protected String          targetId;
  
  @Override
  public List<String> getAllowedEntities()
  {
    if(allowedEntities == null) {
      allowedEntities = new ArrayList<>();
    }
    return allowedEntities;
  }
  
  @Override
  public void setAllowedEntities(List<String> allowedEntities)
  {
    this.allowedEntities = allowedEntities;
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
