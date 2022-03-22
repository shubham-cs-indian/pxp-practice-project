package com.cs.core.config.interactor.entity.klass;

import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface IKlassRelationshipSide extends IRelationshipSide {
  
  public static final String SOURCE_CARDINALITY             = "sourceCardinality";
  public static final String RELATIONSHIP_MAPPING_ID        = "relationshipMappingId";
  public static final String TARGET_RELATIONSHIP_MAPPING_ID = "targetRelationshipMappingId";
  public static final String TARGET_TYPE                    = "targetType";
  public static final String IS_OPPOSITE_VISIBLE            = "isOppositeVisible";
  
  public String getSourceCardinality();
  
  public void setSourceCardinality(String sourceCardinality);
  
  public String getRelationshipMappingId();
  
  public void setRelationshipMappingId(String relationshipMappingId);
  
  public String getTargetRelationshipMappingId();
  
  public void setTargetRelationshipMappingId(String targetRelationshipMappingId);
  
  public String getTargetType();
  
  public void setTargetType(String targetType);
  
  public Boolean getIsOppositeVisible();
  
  public void setIsOppositeVisible(Boolean isOppositeVisible);
}
