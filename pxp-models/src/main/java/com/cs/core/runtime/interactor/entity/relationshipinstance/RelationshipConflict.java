package com.cs.core.runtime.interactor.entity.relationshipinstance;

import com.cs.core.runtime.interactor.entity.configuration.base.AbstractRuntimeEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.List;

public class RelationshipConflict extends AbstractRuntimeEntity implements IRelationshipConflict {
  
  private static final long                      serialVersionUID = 1L;
  protected String                               propagableRelationshipId;
  protected String                               propagableRelationshipSideId;
  protected List<IRelationshipConflictingSource> relationshipConflictingSource;
  protected Boolean                              isResolved       = false;
  
  @Override
  public String getPropagableRelationshipId()
  {
    return propagableRelationshipId;
  }
  
  @Override
  public void setPropagableRelationshipId(String propagableRelationshipId)
  {
    this.propagableRelationshipId = propagableRelationshipId;
  }
  
  @Override
  public String getPropagableRelationshipSideId()
  {
    return propagableRelationshipSideId;
  }
  
  @Override
  public void setPropagableRelationshipSideId(String propagableRelationshipSideId)
  {
    this.propagableRelationshipSideId = propagableRelationshipSideId;
  }
  
  @Override
  public List<IRelationshipConflictingSource> getConflicts()
  {
    if (relationshipConflictingSource == null) {
      relationshipConflictingSource = new ArrayList<>();
    }
    return relationshipConflictingSource;
  }
  
  @Override
  @JsonDeserialize(contentAs = RelationshipConflictingSource.class)
  public void setConflicts(List<IRelationshipConflictingSource> relationshipConflictingSource)
  {
    this.relationshipConflictingSource = relationshipConflictingSource;
  }
  
  @Override
  public Boolean getIsResolved()
  {
    return isResolved;
  }
  
  @Override
  public void setIsResolved(Boolean isResolved)
  {
    this.isResolved = isResolved;
  }
}
