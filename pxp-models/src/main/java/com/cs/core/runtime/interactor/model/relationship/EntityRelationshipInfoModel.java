package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;

import java.util.ArrayList;
import java.util.List;

public class EntityRelationshipInfoModel implements IEntityRelationshipInfoModel {
  
  private static final long      serialVersionUID = 1L;
  
  protected String               relationshipId;
  protected String               sideId;
  protected List<IIdAndBaseType> addedEntities;
  protected List<IIdAndBaseType> removedEntities;
  
  @Override
  public String getRelationshipId()
  {
    return relationshipId;
  }
  
  @Override
  public void setRelationshipId(String relationshipId)
  {
    this.relationshipId = relationshipId;
  }
  
  @Override
  public String getSideId()
  {
    return sideId;
  }
  
  @Override
  public void setSideId(String sideId)
  {
    this.sideId = sideId;
  }
  
  @Override
  public List<IIdAndBaseType> getAddedEntities()
  {
    if (addedEntities == null) {
      addedEntities = new ArrayList<>();
    }
    return addedEntities;
  }
  
  @Override
  public void setAddedEntities(List<IIdAndBaseType> addedEntities)
  {
    this.addedEntities = addedEntities;
  }
  
  @Override
  public List<IIdAndBaseType> getRemovedEntities()
  {
    if (removedEntities == null) {
      removedEntities = new ArrayList<>();
    }
    return removedEntities;
  }
  
  @Override
  public void setRemovedEntities(List<IIdAndBaseType> removedEntities)
  {
    this.removedEntities = removedEntities;
  }
}
