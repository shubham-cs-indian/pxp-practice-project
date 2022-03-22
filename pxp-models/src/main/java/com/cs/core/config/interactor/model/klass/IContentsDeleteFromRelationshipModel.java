package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IContentsDeleteFromRelationshipModel extends IModel {
  
  public static final String SOURCE_CONTENT_ID = "sourceContentId";
  public static final String RELATIONSHIP_ID   = "relationshipId";
  public static final String PROPERTY_IDS      = "propertyIds";
  
  public String getSourceContentId();
  
  public void setSourceContentId(String sourceContentId);
  
  public String getRelationshipId();
  
  public void setRelationshipId(String relationshipId);
  
  public List<String> getPropertyIds();
  
  public void setPropertyIds(List<String> propertyIds);
}
