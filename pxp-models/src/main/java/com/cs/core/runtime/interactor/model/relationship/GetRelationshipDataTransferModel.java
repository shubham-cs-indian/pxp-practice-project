package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.transfer.GetDataTransferModel;

import java.util.HashMap;
import java.util.Map;

public class GetRelationshipDataTransferModel extends GetDataTransferModel
    implements IGetRelationshipDataTransferModel {
  
  private static final long                                     serialVersionUID = 1L;
  
  protected Map<String, IRelationshipDataTransferPropertyModel> relationshipDataTransferDueToAddedEntity;
  protected Map<String, IRelationshipDataTransferPropertyModel> relationshipDataTransferDueToModifiedProperty;
  protected Map<String, IRelationshipDataTransferPropertyModel> relationshipDataInheritanceDueToAddedEntity;
  protected Map<String, IRelationshipDataTransferPropertyModel> relationshipDataInheritanceDueToAddedProperty;
  
  @Override
  public Map<String, IRelationshipDataTransferPropertyModel> getRelationshipDataTransferDueToAddedEntity()
  {
    if (relationshipDataTransferDueToAddedEntity == null) {
      relationshipDataTransferDueToAddedEntity = new HashMap<>();
    }
    return relationshipDataTransferDueToAddedEntity;
  }
  
  @Override
  public void setRelationshipDataTransferDueToAddedEntity(
      Map<String, IRelationshipDataTransferPropertyModel> relationshipDataTransferDueToAddedEntity)
  {
    this.relationshipDataTransferDueToAddedEntity = relationshipDataTransferDueToAddedEntity;
  }
  
  @Override
  public Map<String, IRelationshipDataTransferPropertyModel> getRelationshipDataTransferDueToModifiedProperty()
  {
    if (relationshipDataTransferDueToModifiedProperty == null) {
      relationshipDataTransferDueToModifiedProperty = new HashMap<>();
    }
    return relationshipDataTransferDueToModifiedProperty;
  }
  
  @Override
  public void setRelationshipDataTransferDueToModifiedProperty(
      Map<String, IRelationshipDataTransferPropertyModel> relationshipDataTransferDueToModifiedProperty)
  {
    this.relationshipDataTransferDueToModifiedProperty = relationshipDataTransferDueToModifiedProperty;
  }
  
  @Override
  public Map<String, IRelationshipDataTransferPropertyModel> getRelationshipDataInheritanceDueToAddedProperty()
  {
    if (relationshipDataInheritanceDueToAddedProperty == null) {
      relationshipDataInheritanceDueToAddedProperty = new HashMap<>();
    }
    return relationshipDataInheritanceDueToAddedProperty;
  }
  
  @Override
  public void setRelationshipDataInheritanceDueToAddedProperty(
      Map<String, IRelationshipDataTransferPropertyModel> relationshipDataInheritanceDueToAddedProperty)
  {
    this.relationshipDataInheritanceDueToAddedProperty = relationshipDataInheritanceDueToAddedProperty;
  }
  
  @Override
  public Map<String, IRelationshipDataTransferPropertyModel> getRelationshipDataInheritanceDueToAddedEntity()
  {
    if (relationshipDataInheritanceDueToAddedEntity == null) {
      relationshipDataInheritanceDueToAddedEntity = new HashMap<>();
    }
    return relationshipDataInheritanceDueToAddedEntity;
  }
  
  @Override
  public void setRelationshipDataInheritanceDueToAddedEntity(
      Map<String, IRelationshipDataTransferPropertyModel> relationshipDataInheritanceDueToAddedEntity)
  {
    this.relationshipDataInheritanceDueToAddedEntity = relationshipDataInheritanceDueToAddedEntity;
  }
}
