package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IEntityRelationshipInfoModel extends IModel {
  
  public static final String RELATIONSHIP_ID  = "relationshipId";
  public static final String SIDE_ID          = "sideId";
  public static final String ADDED_ENTITIES   = "addedEntities";
  public static final String REMOVED_ENTITIES = "removedEntities";
  
  public String getRelationshipId();
  
  public void setRelationshipId(String relationshipId);
  
  public String getSideId();
  
  public void setSideId(String sideId);
  
  public List<IIdAndBaseType> getAddedEntities();
  
  public void setAddedEntities(List<IIdAndBaseType> addedEntities);
  
  public List<IIdAndBaseType> getRemovedEntities();
  
  public void setRemovedEntities(List<IIdAndBaseType> removedEntities);
}
