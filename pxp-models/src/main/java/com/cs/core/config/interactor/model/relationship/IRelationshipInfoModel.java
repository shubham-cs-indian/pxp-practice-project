package com.cs.core.config.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IRelationshipInfoModel extends IModel {
  
  public static final String RELATIONSHIP_INFO = "relationshipInfo";
  
  public Map<String, Object> getRelationshipInfo();
  
  public void setRelationshipInfo(Map<String, Object> relationshipInfo);
}
