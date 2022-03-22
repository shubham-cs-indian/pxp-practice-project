package com.cs.core.runtime.interactor.model.instancetree;

import java.util.List;

public interface IGetKlassTaxonomyTreeForRelationshipRequestModel extends IGetKlassTaxonomyTreeRequestModel {
  
  public static final String RELATIONSHIP_ID                      = "relationshipId";
  public static final String SIDE_ID                              = "sideId";
  public static final String TARGET_IDS                           = "targetIds";
  //To differentiate between "relationship" and "reference"
  public static final String TYPE                                 = "type";   
  public static final String INSTANCE_ID                          = "instanceId";
  
  public String getRelationshipId();
  public void setRelationShipId(String relationshipId);
  
  public String getSideId();
  public void setSideId(String sideId);

  public List<String> getTargetIds();
  public void setTargetIds(List<String> targetIds);
  
  public String getType();
  public void setType(String type);
  
  public String getInstanceId();
  public void setInstanceId(String instanceId);
  
}
