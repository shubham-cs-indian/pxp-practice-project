package com.cs.core.runtime.interactor.model.configdetails.instancetree;

import java.util.List;

public interface IConfigDetailsForRelationshipKlassTaxonomyRequestModel extends IConfigDetailsForGetKlassTaxonomyTreeRequestModel {
  
  public static final String ALLOWED_ENTITIES = "allowedEntities";
  public static final String RELATIONSHIP_ID  = "relationshipId";
  public static final String SIDE_ID          = "sideId";
  //To differentiate between "relationship" and "reference"
  public static final String TYPE             = "type";     
  public static final String TARGET_ID        = "targetId";
  
  public List<String> getAllowedEntities();
  public void setAllowedEntities(List<String> allowedEntities);
  
  public String getSideId();
  public void setSideId(String sideId);
  
  public String getRelationshipId();
  public void setRelationshipId(String relationshipId);
  
  public String getType();
  public void setType(String type);
  
  public String getTargetId();
  public void setTargetId(String targetId);
  
}
