package com.cs.core.runtime.interactor.model.goldenrecord;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class RelationshipRecommendationModel extends AbstractRecommendationModel
    implements IRelationshipRecommendationModel {
  
  private static final long                   serialVersionUID = 1L;
  
  protected String                            name;
  protected String                            relationshipId;
  protected List<String>                      klassInstanceIds;
  protected ITargetRelationshipInstancesModel targetRelationshipInstances;
  
  @Override
  public String getName()
  {
    return name;
  }
  
  @Override
  public void setName(String name)
  {
    this.name = name;
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
  public List<String> getKlassInstanceIds()
  {
    if (klassInstanceIds == null) {
      klassInstanceIds = new ArrayList<>();
    }
    return klassInstanceIds;
  }
  
  @Override
  public void setKlassInstanceIds(List<String> klassInstanceIds)
  {
    this.klassInstanceIds = klassInstanceIds;
  }
  
  @Override
  public ITargetRelationshipInstancesModel getTargetRelationshipInstances()
  {
    return targetRelationshipInstances;
  }
  
  @Override
  @JsonDeserialize(as = TargetRelationshipInstancesModel.class)
  public void setTargetRelationshipInstances(
      ITargetRelationshipInstancesModel targetRelationshipInstances)
  {
    this.targetRelationshipInstances = targetRelationshipInstances;
  }
}
