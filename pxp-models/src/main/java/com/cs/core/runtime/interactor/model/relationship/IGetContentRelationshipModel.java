package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetContentRelationshipModel extends IModel {
  
  public static final String ID                      = "id";
  public static final String RELATIONSHIP_MAPPING_ID = "relationshipMappingId";
  
  public String getId();
  
  public void setId(String id);
  
  public String getRelationshipMappingId();
  
  public void setRelationshipMappingId(String relationshipMappingId);
}
