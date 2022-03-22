package com.cs.core.runtime.interactor.entity.relationshipinstance;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IConfigChangeRelationshipInheritanceModel extends IModel{
  
  String RELATIONSHIP_IDS = "relationshipIds";
  
  public List<String> getRelationshipIds();
  public void setRelationshipIds(List<String> relationshipIds);
  
  
}
