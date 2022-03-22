package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IDeleteRelationshipInstancesStrategyModel extends IModel {
  
  public static final String DELETED_RELATIONSHIP_IDS        = "deletedRelationshipIds";
  public static final String DELETED_NATURE_RELATIONSHIP_IDS = "deletedNatureRelationshipIds";
  public static final String MODIFIED_INSTANCES              = "modifiedInstances";
  
  public List<String> getDeletedRelationshipIds();
  
  public void setDeletedRelationshipIds(List<String> deletedRelationshipIds);
  
  public List<String> getDeletedNatureRelationshipIds();
  
  public void setDeletedNatureRelationshipIds(List<String> deletedNatureRelationshipIds);
  
  public List<Map<String, Object>> getModifiedInstances();
  
  public void setModifiedInstances(List<Map<String, Object>> modifiedInstances);
}
