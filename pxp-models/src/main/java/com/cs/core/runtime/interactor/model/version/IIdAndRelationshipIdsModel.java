package com.cs.core.runtime.interactor.model.version;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IIdAndRelationshipIdsModel extends IModel {
  
  public static final String ID               = "id";
  public static final String RELATIONSHIP_IDS = "relationshipIds";
  
  public String getId();
  
  public void setId(String id);
  
  public List<String> getRelationshipIds();
  
  public void setRelationshipIds(List<String> id);
}
