package com.cs.di.workflow.model.relationship;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IRelationshipInstanceModel extends IModel {
  
  public static final String SOURCE_TYPE           = "sourceType";
  public static final String RELATIONSHIP_ID       = "relationshipId";
  public static final String INSTANCE_ID           = "instanceId";
  public static final String OPPOSITE_INSTANCE_IDS = "oppositeInstanceIds";
  public static final String CONTEXT               = "context";
  
  public String getInstanceId();
  
  public void setInstanceId(String instanceId);
  
  public String getRelationshipId();
  
  public void setRelationshipId(String relationshipId);
  
  public String getSourceType();
  
  public void setSourceType(String sourceType);
  
  public List<String> getOppositeInstanceId();
  
  public void setOppositeInstanceId(List<String> oppositeInstanceId);
  
  public List<IRelationshipContextModel> getContext();
  
  public void setContext(List<IRelationshipContextModel> context);
}
