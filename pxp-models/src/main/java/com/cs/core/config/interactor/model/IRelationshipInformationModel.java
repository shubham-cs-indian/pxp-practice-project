package com.cs.core.config.interactor.model;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IRelationshipInformationModel extends IModel {
  
  public static final String RELATIONSHIP_ID        = "relationshipId";
  public static final String RELATIONSHIP_TYPE      = "relationshipType";
  public static final String RELATIONSHIP_SIDE_1_ID = "relationshipSide1Id";
  public static final String RELATIONSHIP_SIDE_2_ID = "relationshipSide2Id";
  public static final String SOURCE_SIDE_ID         = "sourceSideId";
  public static final String TARGET_SIDE_ID         = "targetSideId";
  
  public String getRelationshipId();
  
  public void setRelationshipId(String relationshipId);
  
  public String getRelationshipType();
  
  public void setRelationshipType(String relationshipType);
  
  public String getRelationshipSide1Id();
  
  public void setRelationshipSide1Id(String relationshipSide1Id);
  
  public String getRelationshipSide2Id();
  
  public void setRelationshipSide2Id(String relationshipSide2Id);
  
  public String getSourceSideId();
  
  public void setSourceSideId(String sourceSideId);
  
  public String getTargetSideId();
  
  public void setTargetSideId(String targetSideId);
}
