package com.cs.core.config.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IRelationsIdsModel extends IModel {
  
  String NATURE_RELATIONSHIP_IDS = "natureRelationshipIds";
  
  public List<String> getNatureRelationshipIds();
  
  public void setNatureRelationshipIds(List<String> natureRelationshipIds);
}
