package com.cs.core.runtime.interactor.model.relationship;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeleteRelationshipInstancesStrategyModel
    implements IDeleteRelationshipInstancesStrategyModel {
  
  private static final long           serialVersionUID = 1L;
  protected List<String>              deletedRelationshipIds;
  protected List<String>              deletedNatureRelationshipIds;
  protected List<Map<String, Object>> modifiedInstances;
  
  @Override
  public List<String> getDeletedRelationshipIds()
  {
    if (deletedRelationshipIds == null) {
      deletedRelationshipIds = new ArrayList<>();
    }
    return deletedRelationshipIds;
  }
  
  @Override
  public void setDeletedRelationshipIds(List<String> deletedRelationshipIds)
  {
    this.deletedRelationshipIds = deletedRelationshipIds;
  }
  
  @Override
  public List<String> getDeletedNatureRelationshipIds()
  {
    if (deletedNatureRelationshipIds == null) {
      deletedNatureRelationshipIds = new ArrayList<>();
    }
    return deletedNatureRelationshipIds;
  }
  
  @Override
  public void setDeletedNatureRelationshipIds(List<String> deletedNatureRelationshipIds)
  {
    this.deletedNatureRelationshipIds = deletedNatureRelationshipIds;
  }
  
  @Override
  public List<Map<String, Object>> getModifiedInstances()
  {
    if (modifiedInstances == null) {
      modifiedInstances = new ArrayList<>();
    }
    return modifiedInstances;
  }
  
  @Override
  public void setModifiedInstances(List<Map<String, Object>> modifiedInstances)
  {
    this.modifiedInstances = modifiedInstances;
  }
}
