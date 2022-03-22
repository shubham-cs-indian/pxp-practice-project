package com.cs.core.runtime.interactor.model.goldenrecord;

import java.util.List;

public interface IRelationshipRecommendationModel extends IRecommendationModel {
  
  public static final String RELATIONSHIP_ID               = "relationshipId";
  public static final String KLASS_INSTANCE_IDS            = "klassInstanceIds";
  public static final String TARGET_RELATIONSHIP_INSTANCES = "targetRelationshipInstances";
  
  public String getName();
  
  public void setName(String name);
  
  public String getRelationshipId();
  
  public void setRelationshipId(String relationshipId);
  
  public List<String> getKlassInstanceIds();
  
  public void setKlassInstanceIds(List<String> klassInstanceIds);
  
  public ITargetRelationshipInstancesModel getTargetRelationshipInstances();
  
  public void setTargetRelationshipInstances(
      ITargetRelationshipInstancesModel targetRelationshipInstances);
}
