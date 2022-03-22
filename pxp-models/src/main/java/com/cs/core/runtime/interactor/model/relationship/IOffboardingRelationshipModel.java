package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IOffboardingRelationshipModel extends IModel {
  
  public static final String SOURCE_ID       = "sourceId";
  public static final String DESTINATION_ID  = "destinationId";
  public static final String RELATIONSHIP_ID = "relationshipId";
  
  public String getSourceId();
  
  public void setSourceId(String sourceId);
  
  public String getDestinationId();
  
  public void setDestinationId(String destinationId);
  
  public String getRelationshipId();
  
  public void setRelationshipId(String relationshipId);
}
