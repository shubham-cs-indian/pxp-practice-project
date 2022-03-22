package com.cs.core.runtime.interactor.entity.relationshipinstance;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class InitiateRelationshipInheritanceRequestModel   implements IInitiateRelationshipInheritanceRequestModel {
  
  private static final long                                       serialVersionUID = 1L;
  
  protected String                                                targetId;
  protected List<IRelationshipConflictingModel>                   resolvedConflicts;
  
 
  
  @Override
  public List<IRelationshipConflictingModel> getResolvedConflicts()
  {
    if (resolvedConflicts == null) {
      resolvedConflicts = new ArrayList<>();
    }
    return resolvedConflicts;
  }
  
  @Override
  @JsonDeserialize(contentAs = RelationshipConflictingModel.class)
  public void setResolvedConflicts(List<IRelationshipConflictingModel> resolvedConflicts)
  {
    this.resolvedConflicts = resolvedConflicts;
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
