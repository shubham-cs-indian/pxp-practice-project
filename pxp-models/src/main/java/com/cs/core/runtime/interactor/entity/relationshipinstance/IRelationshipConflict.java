package com.cs.core.runtime.interactor.entity.relationshipinstance;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import java.util.List;

public interface IRelationshipConflict extends IEntity {
  
  String PROPAGABLE_RELATIONSHIP_ID      = "propagableRelationshipId";
  String PROPAGABLE_RELATIONSHIP_SIDE_ID = "propagableRelationshipSideId";
  String CONFLICTS                       = "conflicts";
  String IS_RESOLVED                     = "isResolved";
  
  public String getPropagableRelationshipId();
  
  public void setPropagableRelationshipId(String propagableRelationshipId);
  
  public String getPropagableRelationshipSideId();
  
  public void setPropagableRelationshipSideId(String propagableRelationshipSideId);
  
  public List<IRelationshipConflictingSource> getConflicts();
  
  public void setConflicts(List<IRelationshipConflictingSource> relationshipConflictingSource);
  
  public Boolean getIsResolved();
  
  public void setIsResolved(Boolean isResolved);
}
