package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.transfer.IGetDataTransferModel;

import java.util.Map;

public interface IGetRelationshipDataTransferModel extends IGetDataTransferModel {
  
  public static final String RELATIONSHIP_DATA_TRANSFER_DUE_TO_ADDED_ENTITY      = "relationshipDataTransferDueToAddedEntity";
  public static final String RELATIONSHIP_DATA_TRANSFER_DUE_TO_MODIFIED_PROPERTY = "relationshipDataTransferDueToModifiedProperty";
  public static final String RELATIONSHIP_DATA_INHERITANCE_DUE_TO_ADDED_ENTITY   = "relationshipDataInheritanceDueToAddedEntity";
  public static final String RELATIONSHIP_DATA_INHERITANCE_DUE_TO_ADDED_PROPERTY = "relationshipDataInheritanceDueToAddedProperty";
  
  // key: relationshipId + RELATIONSHIP_SIDE_SPERATOR + sideId
  public Map<String, IRelationshipDataTransferPropertyModel> getRelationshipDataTransferDueToAddedEntity();
  
  public void setRelationshipDataTransferDueToAddedEntity(
      Map<String, IRelationshipDataTransferPropertyModel> relationshipDataTransferDueToAddedEntity);
  
  // key: relationshipId + RELATIONSHIP_SIDE_SPERATOR + sideId
  public Map<String, IRelationshipDataTransferPropertyModel> getRelationshipDataTransferDueToModifiedProperty();
  
  public void setRelationshipDataTransferDueToModifiedProperty(
      Map<String, IRelationshipDataTransferPropertyModel> relationshipDataTransferDueToModifiedProperty);
  
  // key: relationshipId + RELATIONSHIP_SIDE_SPERATOR + sideId
  public Map<String, IRelationshipDataTransferPropertyModel> getRelationshipDataInheritanceDueToAddedProperty();
  
  public void setRelationshipDataInheritanceDueToAddedProperty(
      Map<String, IRelationshipDataTransferPropertyModel> relationshipDataInheritanceDueToAddedProperty);
  
  // key: relationshipId + RELATIONSHIP_SIDE_SPERATOR + sideId
  public Map<String, IRelationshipDataTransferPropertyModel> getRelationshipDataInheritanceDueToAddedEntity();
  
  public void setRelationshipDataInheritanceDueToAddedEntity(
      Map<String, IRelationshipDataTransferPropertyModel> relationshipDataInheritanceDueToAddedEntity);
}
