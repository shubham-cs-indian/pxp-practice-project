package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.model.transfer.IDataTransferPropertyModel;

import java.util.List;

public interface IRelationshipDataTransferPropertyModel extends IDataTransferPropertyModel {
  
  public static final String RELATIONSHIP_ID  = "relationshipId";
  public static final String CONTENT_ID       = "contentId";
  public static final String ADDED_ENTITIES   = "addedEntities";
  public static final String BASE_TYPE        = "baseType";
  public static final String SIDE_ID          = "sideId";
  public static final String REMOVED_ENTITIES = "removedEntities";
  
  public String getRelationshipId();
  
  public void setRelationshipId(String relationshipId);
  
  public String getContentId();
  
  public void setContentId(String contentId);
  
  public List<IIdAndBaseType> getAddedEntities();
  
  public void setAddedEntities(List<IIdAndBaseType> addedEntities);
  
  public String getBaseType();
  
  public void setBaseType(String baseTye);
  
  public String getSideId();
  
  public void setSideId(String sideId);
  
  public List<IIdAndBaseType> getRemovedEntities();
  
  public void setRemovedEntities(List<IIdAndBaseType> removedEntities);
}
