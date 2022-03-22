package com.cs.di.config.interactor.model.configdetails;

import java.util.Set;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetConfigDataForTransformRequestModel extends IModel {
  
  public static final String CONTEXT_IDS      = "contextIds";
  public static final String RELATIONSHIP_IDS = "relationshipIds";
  
  public Set<String> getContextIds();
  public void setContextIds(Set<String> contextIds);
  
  public Set<String> getRelationshipIds();
  public void setRelationshipIds(Set<String> relationshipIds);
}
