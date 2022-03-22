package com.cs.core.runtime.interactor.entity.relationshipinstance;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class RelationshipConflictingModel implements IRelationshipConflictingModel {
  
  private static final long                      serialVersionUID = 1L;
  protected String                               propagableRelationshipId;
  protected String                               propagableRelationshipSideId;
  protected List<IRelationshipConflictingSource> relationshipConflictingSource;
  protected Boolean                              isResolved = false;
  
  
  public RelationshipConflictingModel()
  {
    
  }
  
  public RelationshipConflictingModel(IRelationshipConflictingModel model)
  {
    this.isResolved = model.getIsResolved();
    this.propagableRelationshipId = model.getPropagableRelationshipId();
    this.propagableRelationshipSideId = model.getPropagableRelationshipSideId();
    this.relationshipConflictingSource = model.getConflicts();
  }
  
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

  @Override
  @JsonIgnore
  public String getId()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  @JsonIgnore
  public void setId(String id)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  @JsonIgnore
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  @JsonIgnore
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  @JsonIgnore
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  @JsonIgnore
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  @JsonIgnore
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  @JsonIgnore
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
}
