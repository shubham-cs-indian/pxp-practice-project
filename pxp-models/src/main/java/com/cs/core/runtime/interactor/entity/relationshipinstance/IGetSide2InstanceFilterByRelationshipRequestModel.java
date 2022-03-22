package com.cs.core.runtime.interactor.entity.relationshipinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import java.util.Map;

public interface IGetSide2InstanceFilterByRelationshipRequestModel extends IModel {
  
  public static final String REFERENCED_RELATIONSHIPS = "referencedRelationships";
  public static final String RELATIONSHIP_ID          = "relationshipId";
  public static final String SIDE_ID                  = "sideId";
  
  public String getRelationshipId();
  
  public void setRelationshipId(String relationshipId);
  
  public String getSideId();
  
  public void setSideId(String sideId);
  
  public Map<String, IReferencedRelationshipInheritanceModel> getReferencedRelationships();
  
  public void setReferencedRelationships(
      Map<String, IReferencedRelationshipInheritanceModel> referencedRelationship);
}
