package com.cs.core.runtime.interactor.model.instancetree;

import java.util.List;

public interface IGetFilterAndSortDataForRelationshipQuicklistRequestModel extends IGetNewFilterAndSortDataRequestModel {
  
  public static final String INSTANCE_ID        = "instanceId";
  public static final String RELATIONSHIP_ID    = "relationshipId";
  public static final String TYPE               = "type";
  public static final String SIDE_ID            = "sideId";
  public static final String TARGET_IDS         = "targetIds";
  public static final String IS_TARGET_TAXONOMY = "isTargetTaxonomy";
  
  public String getInstanceId();
  public void setInstanceId(String instanceId);
  
  public String getRelationshipId();
  public void setRelationshipId(String relationshipId);
  
  public String getType();
  public void setType(String type);
  
  //targetSideId
  public String getSideId();
  public void setSideId(String sideId);
  
  public List<String> getTargetIds();
  public void setTargetIds(List<String> targetIds);
  
  public Boolean getIsTargetTaxonomy();
  public void setIsTargetTaxonomy(Boolean isTargetTaxonomy);
  
}
