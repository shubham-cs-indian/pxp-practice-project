package com.cs.core.runtime.interactor.entity.relationshipinstance;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IInitiateRelationshipInheritanceRequestModel extends IModel {
  
  String TARGET_ID                                        = "targetId";
  String RESOLVED_CONFLICTS                               = "resolvedConflicts";
  
  public String getTargetId();
  public void setTargetId(String targetId);
  
  public List<IRelationshipConflictingModel> getResolvedConflicts();
  public void setResolvedConflicts(List<IRelationshipConflictingModel> resolvedConflicts);
  
}
