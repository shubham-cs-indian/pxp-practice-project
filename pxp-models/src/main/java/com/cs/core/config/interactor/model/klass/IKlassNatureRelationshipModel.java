package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.klass.IKlassNatureRelationship;

public interface IKlassNatureRelationshipModel extends IKlassNatureRelationship {
  
  public static final String TARGET_RELATIONSHIP_MAPPING_ID = "targetRelationshipMappingId";
  
  public String getTargetRelationshipMappingId();
  
  public void setTargetRelationshipMappingId(String targetRelationshipMappingId);
}
