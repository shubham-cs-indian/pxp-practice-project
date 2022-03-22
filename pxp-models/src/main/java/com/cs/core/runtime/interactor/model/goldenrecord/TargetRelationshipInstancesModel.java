package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.config.interactor.entity.relationship.IRelationshipInstance;
import com.cs.core.config.interactor.entity.relationship.RelationshipInstance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class TargetRelationshipInstancesModel implements ITargetRelationshipInstancesModel {
  
  private static final long             serialVersionUID = 1L;
  protected List<IRelationshipInstance> relationshipInstances;
  protected Long                        totalCount       = 0l;
  
  @Override
  public List<IRelationshipInstance> getRelationshipInstances()
  {
    return relationshipInstances;
  }
  
  @JsonDeserialize(contentAs = RelationshipInstance.class)
  @Override
  public void setRelationshipInstances(List<IRelationshipInstance> relationshipInstances)
  {
    this.relationshipInstances = relationshipInstances;
  }
  
  @Override
  public Long getTotalCount()
  {
    return totalCount;
  }
  
  @Override
  public void setTotalCount(Long totalCount)
  {
    this.totalCount = totalCount;
  }
}
