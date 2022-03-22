package com.cs.core.runtime.interactor.model.klassinstance;

import java.util.List;

public class GetKlassInstanceRelationshipTreeStrategyModel
    implements IGetKlassInstanceRelationshipTreeStrategyModel {
  
  protected String       id;
  protected String       relationshipId;
  protected String       targetType;
  protected List<String> contentIds;
  
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
  public String getTargetType()
  {
    return targetType;
  }
  
  @Override
  public void setTargetType(String targetType)
  {
    this.targetType = targetType;
  }
  
  @Override
  public List<String> getContentIds()
  {
    return contentIds;
  }
  
  @Override
  public void setContentIds(List<String> contentIds)
  {
    this.contentIds = contentIds;
  }
}
