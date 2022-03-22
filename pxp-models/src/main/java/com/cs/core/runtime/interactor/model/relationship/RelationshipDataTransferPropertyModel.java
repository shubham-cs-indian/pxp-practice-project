package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.model.transfer.DataTransferPropertyModel;

import java.util.ArrayList;
import java.util.List;

public class RelationshipDataTransferPropertyModel extends DataTransferPropertyModel
    implements IRelationshipDataTransferPropertyModel {
  
  private static final long      serialVersionUID = 1L;
  
  protected String               relationshipId;
  protected String               contentId;
  protected List<IIdAndBaseType> addedEntities;
  protected String               baseTye;
  protected String               sideId;
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
  public String getContentId()
  {
    return contentId;
  }
  
  @Override
  public void setContentId(String contentId)
  {
    this.contentId = contentId;
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
  public String getBaseType()
  {
    return baseTye;
  }
  
  @Override
  public void setBaseType(String baseTye)
  {
    this.baseTye = baseTye;
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
