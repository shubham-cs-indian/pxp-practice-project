package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetKlassInstanceRelationshipTreeStrategyModel extends IModel {
  
  public static final String ID              = "id";
  public static final String TARGET_TYPE     = "targetType";
  public static final String CONTENT_IDS     = "contentIds";
  public static final String RELATIONSHIP_ID = "relationshipId";
  
  public String getId();
  
  public void setId(String id);
  
  public String getTargetType();
  
  public void setTargetType(String targetType);
  
  public List<String> getContentIds();
  
  public void setContentIds(List<String> contentIds);
  
  public String getRelationshipId();
  
  public void setRelationshipId(String relationshipId);
}
