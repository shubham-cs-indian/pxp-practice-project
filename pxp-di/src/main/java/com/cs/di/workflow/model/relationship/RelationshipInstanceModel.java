package com.cs.di.workflow.model.relationship;

import java.util.ArrayList;
import java.util.List;

public class RelationshipInstanceModel implements IRelationshipInstanceModel {
  
  private static final long                serialVersionUID = 1L;
  
  protected String                               instanceId;
  protected String                               relationshipId;
  protected String                               sourceType;
  private List<String>                           oppositeInstanceIds;
  private List<IRelationshipContextModel> context;
  
  @Override
  public List<String> getOppositeInstanceId()
  {
    return this.oppositeInstanceIds;
  }
  
  @Override
  public void setOppositeInstanceId(List<String> oppositeInstanceIds)
  {
    this.oppositeInstanceIds = oppositeInstanceIds;
  }
  
  @Override
  public List<IRelationshipContextModel> getContext()
  {
    if (this.context == null) {
      this.context = new ArrayList<>();
    }
    return this.context;
  }
  
  @Override
  public void setContext(List<IRelationshipContextModel> context)
  {
    this.context = context;
  }

  @Override
  public String getInstanceId()
  {
    return instanceId;
  }
  
  @Override
  public void setInstanceId(String instanceId)
  {
    this.instanceId = instanceId;
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
  
  public String getSourceType()
  {
    return sourceType;
  }
  
  public void setSourceType(String sourceType)
  {
    this.sourceType = sourceType;
  }
}
