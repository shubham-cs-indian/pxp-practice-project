package com.cs.core.config.interactor.model.relationship;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

public class RelationshipInfoModel implements IRelationshipInfoModel {
  
  private static final long     serialVersionUID = 1L;
  protected Map<String, Object> relationshipInfo;
  
  @Override
  public Map<String, Object> getRelationshipInfo()
  {
    return relationshipInfo;
  }
  
  @Override
  @JsonDeserialize(as = HashMap.class)
  public void setRelationshipInfo(Map<String, Object> relationshipInfo)
  {
    this.relationshipInfo = relationshipInfo;
  }
}
