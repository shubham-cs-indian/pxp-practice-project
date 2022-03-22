package com.cs.core.runtime.interactor.model.component.klassinstance;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class DiRelationshipsModel implements IDiRelationshipsModel {
  
  private static final long serialVersionUID = 1L;
  private String            id;
  private String            relationshipId;
  private String            sourceId;
  private String            targetId;
  private IDiOptionalModel  optional;
  
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
  public String getRelationshipId()
  {
    return this.relationshipId;
  }
  
  @Override
  public void setRelationshipId(String relationshipId)
  {
    this.relationshipId = relationshipId;
  }
  
  @Override
  public String getSide1Id()
  {
    return this.sourceId;
  }
  
  @Override
  public void setSide1Id(String sourceId)
  {
    this.sourceId = sourceId;
  }
  
  @Override
  public String getSide2Id()
  {
    return this.targetId;
  }
  
  @Override
  public void setSide2Id(String targetId)
  {
    this.targetId = targetId;
  }
  
  @Override
  public IDiOptionalModel getOptional()
  {
    if (this.optional == null) {
      this.optional = new DiOptionalModel();
    }
    return this.optional;
  }
  
  @Override
  @JsonDeserialize(as = DiOptionalModel.class)
  public void setOptional(IDiOptionalModel optional)
  {
    this.optional = optional;
  }
}
